package com.demo.ubike.data.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.R
import com.demo.ubike.data.model.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.exceptions.CompositeException
import io.reactivex.plugins.RxJavaPlugins
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    init {
        setErrorHandler()
    }

    // first: error enum
    // second: 錯誤訊息
    private var _exceptionEvent = MutableLiveData<Pair<Error, String>>()
    val exceptionEvent: LiveData<Pair<Error, String>> = _exceptionEvent

    private fun setErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            var error: Pair<Error, String>? = null
            if (it is CompositeException) {
                it.exceptions.forEach { e ->
                    error = when (e) {
                        is HttpException -> {
                            if (e.message?.contains("HTTP 401 Unauthorized") == true) {
                                Pair(
                                    Error.ERR00001,
                                    context.getString(R.string.error00001, e.message)
                                )
                            } else {
                                val errorMsg = e.response()?.errorBody()?.string() ?: e.message
                                Pair(
                                    Error.ERR00002,
                                    context.getString(R.string.error00002, errorMsg)
                                )
                            }
                        }

                        is UnknownHostException -> {
                            Pair(Error.ERR00003, context.getString(R.string.error00003, e.message))
                        }

                        is SocketTimeoutException -> {
                            Pair(Error.ERR00004, context.getString(R.string.error00004, e.message))
                        }

                        else -> {
                            Pair(Error.ERR00005, context.getString(R.string.error00005, e.message))
                        }
                    }
                }
            } else {
                error = Pair(Error.ERR99999, context.getString(R.string.error99999, it.message))
            }

            error?.let { pair ->
                _exceptionEvent.postValue(pair)
            }
        }
    }
}