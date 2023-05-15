package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private var _goHomePage: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val goHomePage: LiveData<Boolean> = _goHomePage
    fun fetchToken() {
        fetchTokenUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                sharePreferenceManager.token = it.access_token
                _goHomePage.value = true
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }
}