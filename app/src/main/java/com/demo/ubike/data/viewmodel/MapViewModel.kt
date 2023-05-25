package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.usecase.FetchAllStationAndInsertUseCase
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetStationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val fetchStationDetailUseCase: FetchStationDetailUseCase,
    private val fetchAllStationAndInsertUseCase: FetchAllStationAndInsertUseCase,
    private val getStationsUseCase: GetStationsUseCase
) : BaseViewModel() {
    private val _stations = MutableLiveData<List<StationEntity>>()
    var stations: LiveData<List<StationEntity>> = _stations

    private val _stationDetail = MutableLiveData<StationDetailResponse>()
    var stationDetail: LiveData<StationDetailResponse> = _stationDetail

    private var disposable: Disposable? = null

    fun fetchAllStationAndInsert() {
        fetchAllStationAndInsertUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // nothing
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun getStations(lat: Double, lon: Double) {
        disposable = getStationsUseCase(lat, lon)
            .delay(400, TimeUnit.MILLISECONDS)
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

    fun cancelGetStations() {
        disposable?.dispose()
    }

    fun fetchStationDetail(city: City, stationId: String) {
        fetchStationDetailUseCase.byStationUid(city, stationId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _stationDetail.value = it
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }

    }
}