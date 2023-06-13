package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.model.ApiResult
import com.demo.ubike.usecase.FetchTokenUseCase
import com.demo.ubike.utils.SharePreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchTokenUseCase: FetchTokenUseCase,
    private val sharePreferenceManager: SharePreferenceManager
) : BaseViewModel() {
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
}