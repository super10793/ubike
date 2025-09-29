package com.demo.ubike.network

import com.demo.ubike.result.Result
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return when (getRawType(returnType)) {
            Call::class.java -> {
                val callType = getParameterUpperBound(0, returnType as ParameterizedType)
                when (getRawType(callType)) {
                    Result::class.java -> {
                        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                        ResultCallAdapter(resultType)
                    }

                    else -> null
                }
            }

            else -> null
        }
    }
}
