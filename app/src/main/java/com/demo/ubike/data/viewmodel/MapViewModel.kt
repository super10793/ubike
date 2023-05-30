package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.usecase.AddFavoriteUseCase
import com.demo.ubike.usecase.FavoriteIsExistUseCase
import com.demo.ubike.usecase.FetchAllStationAndInsertUseCase
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetStationsUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import com.demo.ubike.utils.Event
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
    private val getStationsUseCase: GetStationsUseCase,
    private val favoriteAddUseCase: AddFavoriteUseCase,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
    private val favoriteIsExistUseCase: FavoriteIsExistUseCase,
) : BaseViewModel() {
    private val _stations = MutableLiveData<List<StationEntity>>()
    var stations: LiveData<List<StationEntity>> = _stations

    private val _stationDetail = MutableLiveData<Event<StationDetailResponse>>()
    var stationDetail: LiveData<Event<StationDetailResponse>> = _stationDetail

    // first: station的Uid
    // second: 是否為最愛
    private val _isFavorite = MutableLiveData<Event<Pair<String, Boolean>>>()
    var isFavorite: LiveData<Event<Pair<String, Boolean>>> = _isFavorite

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
                _stationDetail.value = Event(it)
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun favoriteIsExist(stationUid: String) {
        favoriteIsExistUseCase(stationUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isFavorite.value = Event(Pair(stationUid, it))
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun addFavorite(entity: FavoriteEntity) {
        favoriteAddUseCase(entity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isFavorite.value = Event(Pair(entity.stationUID, true))
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun removeFavorite(stationUid: String) {
        favoriteRemoveUseCase(stationUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _isFavorite.value = Event(Pair(stationUid, false))
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }
}