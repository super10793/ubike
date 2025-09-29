package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.result.Event
import com.demo.ubike.result.Result
import com.demo.ubike.result.succeeded
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetAllFavoriteUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getAllFavoriteUseCase: GetAllFavoriteUseCase,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
    private val fetchStationDetailUseCase: FetchStationDetailUseCase
) : BaseViewModel() {

    private val _favoriteList = MutableLiveData<List<FavoriteEntity>>()
    val favoriteList: LiveData<List<FavoriteEntity>> = _favoriteList

    private val _favoriteRemoved = MutableLiveData<Event<Boolean>>()
    val favoriteRemoved: LiveData<Event<Boolean>> = _favoriteRemoved

    private val _stationDetail = MutableLiveData<StationDetailResponse.Data>()
    val stationDetail: LiveData<StationDetailResponse.Data> = _stationDetail

    fun getAllFavorite() {
        getAllFavoriteUseCase(Unit)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _favoriteList.value = result.data
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
            .onEach { _favoriteRemoved.value = Event(it.succeeded) }
            .launchIn(viewModelScope)
    }

    fun fetchStationDetail(city: City, stationUid: String) {
        val parameters = FetchStationDetailUseCase.Parameters(city.apiKey, stationUid)
        fetchStationDetailUseCase(parameters)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stationDetail.value = result.data
                    }

                    is Result.Error -> {
                        // todo
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}