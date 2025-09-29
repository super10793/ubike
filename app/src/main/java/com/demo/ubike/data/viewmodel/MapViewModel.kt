package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.result.Event
import com.demo.ubike.result.Result
import com.demo.ubike.usecase.AddFavoriteUseCase
import com.demo.ubike.usecase.FavoriteIsExistUseCase
import com.demo.ubike.usecase.FetchAllStationAndInsertUseCase
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetStationsUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private val _stationDetail = MutableLiveData<Event<StationDetailResponse.Data>>()
    var stationDetail: LiveData<Event<StationDetailResponse.Data>> = _stationDetail

    // first: station的Uid
    // second: 是否為最愛
    private val _isFavorite = MutableLiveData<Event<Pair<String, Boolean>>>()
    var isFavorite: LiveData<Event<Pair<String, Boolean>>> = _isFavorite

    private var runningJob: Job? = null

    fun fetchAllStationAndInsert() {
        fetchAllStationAndInsertUseCase(Unit)
            .onEach {
                // todo
            }
            .launchIn(viewModelScope)
    }

    fun getStations(lat: Double, lon: Double) {
        val parameters = GetStationsUseCase.Parameters(lat, lon)
        runningJob?.cancel()
        runningJob = getStationsUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stations.value = result.data
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun cancelGetStations() {
        runningJob?.cancel()
    }

    fun fetchStationDetail(city: City, stationId: String) {
        val parameters = FetchStationDetailUseCase.Parameters(city.apiKey, stationId)
        fetchStationDetailUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stationDetail.value = Event(result.data)
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun favoriteIsExist(stationUid: String) {
        val parameters = FavoriteIsExistUseCase.Parameters(stationUid)
        favoriteIsExistUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _isFavorite.value = Event(Pair(stationUid, result.data))
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun addFavorite(entity: FavoriteEntity) {
        val parameters = AddFavoriteUseCase.Parameters(entity)
        favoriteAddUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _isFavorite.value = Event(Pair(entity.stationUID, true))
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeFavorite(stationUid: String) {
        val parameters = RemoveFavoriteUseCase.Parameters(stationUid)
        favoriteRemoveUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _isFavorite.value = Event(Pair(stationUid, false))
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}