package com.demo.ubike.data.viewmodel

import io.reactivex.exceptions.CompositeException
import io.reactivex.plugins.RxJavaPlugins

class MainViewModel : BaseViewModel() {
    init {
        setErrorHandler()
    }

    private fun setErrorHandler() {
        RxJavaPlugins.setErrorHandler {
            if (it !is CompositeException) return@setErrorHandler
            it.exceptions.forEach { error ->
                println("setErrorHandler error = $error")
            }
        }
    }
}