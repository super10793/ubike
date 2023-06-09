package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.model.ApiResult
import com.demo.ubike.usecase.FetchTokenUseCase
import com.demo.ubike.utils.SharePreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchTokenUseCase: FetchTokenUseCase,
    private val sharePreferenceManager: SharePreferenceManager
) : BaseViewModel() {
    private var _progressComplete: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val progressComplete: LiveData<Boolean> = _progressComplete

    private val _progress = MutableLiveData(0)
    val progress: LiveData<Int> = _progress

    private var _fetchTokenResult = MutableLiveData<ApiResult>()
    val fetchTokenResult: LiveData<ApiResult> = _fetchTokenResult

    fun fetchToken() {
        fetchTokenUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _fetchTokenResult.value = ApiResult.Success(it)
                sharePreferenceManager.token = it.access_token
            }, {
                _fetchTokenResult.value = ApiResult.Fail(it)
            })
            .also {
                addDisposable(it)
            }
    }

    fun startProgress() {
        val disposable = Observable.intervalRange(0, 101, 0, 10, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateProgress(it.toInt())
                },
                {
                    it.printStackTrace()
                }
            ) {
                _progressComplete.value = true
            }
    }

    private fun updateProgress(progress: Int) {
        _progress.value = progress
    }

    fun resetProgress() {
        _progress.value = 0
        _progressComplete.value = false
    }
}