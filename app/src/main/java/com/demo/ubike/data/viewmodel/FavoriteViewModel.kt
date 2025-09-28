package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetAllFavoriteUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import com.demo.ubike.result.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
    private val fetchStationDetailUseCase: FetchStationDetailUseCase
) : BaseViewModel() {
    // key: stationId
    private val disposeMap = hashMapOf<String, Disposable>()

    private val _favoriteList = MutableLiveData<List<FavoriteEntity>>()
    val favoriteList: LiveData<List<FavoriteEntity>> = _favoriteList

    private val _favoriteRemoved = MutableLiveData<Event<Boolean>>()
    val favoriteRemoved: LiveData<Event<Boolean>> = _favoriteRemoved

    private val _stationDetails = MutableLiveData<List<StationDetailResponse.Data>>()
    val stationDetails: LiveData<List<StationDetailResponse.Data>> = _stationDetails

    fun getAllFavorite() {
        getAllFavoriteUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _favoriteList.value = it
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
                _favoriteRemoved.value = Event(true)
            }, {
                throw it
            })
            .also {
                addDisposable(it)
            }
    }

    fun fetchStationDetail(city: City, stationUid: String) {
        fetchStationDetailUseCase.byStationUid(city, stationUid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _stationDetails.value = it
            }, {
                throw it
            })
            .also {
                disposeMap[stationUid] = it
                addDisposable(it)
            }
    }

    fun cancelFetchStationDetail(stationUid: String) {
        disposeMap[stationUid]?.dispose()
        disposeMap.remove(stationUid)
    }
}