package com.demo.ubike.network

import com.demo.ubike.result.Result
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ResultCallAdapter(
    private val type: Type
) : CallAdapter<Type, Any> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Any = ResultCall(call)
}

class ResultCall<T>(
    private val delegate: Call<T>
) : Call<Result<T>> {

    override fun enqueue(callback: Callback<Result<T>>) {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = try {
                    processResponse(response)
                } catch (t: Throwable) {
                    Result.Error(processFailure(t))
                }

                callback.onResponse(this@ResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = Result.Error(processFailure(t))
                callback.onResponse(this@ResultCall, Response.success(result))
            }
        })
    }

    override fun execute(): Response<Result<T>> {
        val result = try {
            val response = delegate.execute()
            processResponse(response)
        } catch (t: Throwable) {
            Result.Error(processFailure(t))
        }
        return Response.success(result)
    }

    override fun clone(): Call<Result<T>> = ResultCall(delegate.clone())
    override fun cancel() = delegate.cancel()
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()

    private fun processResponse(response: Response<T>): Result<T> {
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(ApiException.InvalidBody())
            }
        } else {
            Result.Error(parseErrorBody(response))
        }
    }

    private fun processFailure(throwable: Throwable): ApiException {
        return when (throwable) {
            is UnknownHostException -> ApiException.BadNetwork("No internet connection")
            is ConnectException -> ApiException.BadNetwork("Cannot connect to server")
            is SocketTimeoutException -> ApiException.Timeout("Request timeout")
            is SocketException -> ApiException.Timeout("Network error")
            is IOException -> ApiException.BadNetwork("IO error: ${throwable.message}")
            else -> ApiException.Unknown("Unexpected error: ${throwable.message}")
        }
    }

    private fun parseErrorBody(response: Response<T>): ApiException {
        val httpCode = response.code()
        val errorBodyString = response.errorBody()?.string()
        val defaultMessage = errorBodyString ?: response.message()

        return try {
            if (errorBodyString.isNullOrEmpty()) {
                return ApiException.Http(httpCode, defaultMessage)
            }

            if (errorBodyString.lowercase().contains("invalid token")) {
                return ApiException.InvalidToken()
            }

            val gson = Gson()
            val jsonObject = gson.fromJson(errorBodyString, JsonObject::class.java)

            when {
                jsonObject.has("error") -> {
                    val error = jsonObject.get("error").asString
                    val description = if (jsonObject.has("error_description")) {
                        jsonObject.get("error_description").asString
                    } else null

                    ApiException.Http(httpCode, description ?: error ?: errorBodyString)
                }

                jsonObject.has("Message") -> {
                    val message = jsonObject.get("Message").asString
                    ApiException.Http(httpCode, message)
                }

                jsonObject.has("message") -> {
                    val message = jsonObject.get("message").asString
                    ApiException.Http(httpCode, message)
                }

                else -> ApiException.Http(httpCode, errorBodyString)
            }
        } catch (e: Exception) {
            ApiException.Http(httpCode, e.message ?: errorBodyString ?: defaultMessage)
        }
    }
}
