package com.demo.ubike.data.viewmodel

import com.demo.ubike.data.model.City
import com.demo.ubike.usecase.FetchStationAndInsertUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchStationAndInsertUseCase: FetchStationAndInsertUseCase
) : BaseViewModel() {
    fun fetchStationAndInsert(city: City = City.Taichung) {
        fetchStationAndInsertUseCase(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // success
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }
}