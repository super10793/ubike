package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.usecase.FetchStationAndInsertUseCase
import com.demo.ubike.usecase.GetStationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchStationAndInsertUseCase: FetchStationAndInsertUseCase,
    private val getStationsUseCase: GetStationsUseCase
) : BaseViewModel() {
    private val _stations = MutableLiveData<List<StationEntity>>()
    var stations: LiveData<List<StationEntity>> = _stations

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

    fun getStations(lat: Double, lon: Double) {
        getStationsUseCase(lat, lon)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _stations.value = it
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }
}