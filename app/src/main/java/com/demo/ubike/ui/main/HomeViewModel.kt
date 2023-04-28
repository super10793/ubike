package com.demo.ubike.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchFakeDataUseCase: FetchFakeDataUseCase
) : BaseViewModel() {
    private var _apiResult = MutableLiveData<FakeData>()
    val apiResult: LiveData<FakeData> = _apiResult

    fun fetchData() {
        fetchFakeDataUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _apiResult.value = it
                },
                {
                    throw it
                }
            )
            .also {
                addDisposable(it)
            }
    }
}