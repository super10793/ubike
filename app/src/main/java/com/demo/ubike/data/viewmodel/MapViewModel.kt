package com.demo.ubike.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.ubike.R
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.vo.FavoriteExistVO
import com.demo.ubike.data.model.vo.StationDetailVO
import com.demo.ubike.data.model.vo.StationVO
import com.demo.ubike.network.ApiException
import com.demo.ubike.result.Event
import com.demo.ubike.result.Result
import com.demo.ubike.result.data
import com.demo.ubike.result.mapError
import com.demo.ubike.usecase.AddFavoriteUseCase
import com.demo.ubike.usecase.FavoriteIsExistUseCase
import com.demo.ubike.usecase.FetchAllStationAndInsertUseCase
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetStationsUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fetchStationDetailUseCase: FetchStationDetailUseCase,
    private val fetchAllStationAndInsertUseCase: FetchAllStationAndInsertUseCase,
    private val getStationsUseCase: GetStationsUseCase,
    private val favoriteAddUseCase: AddFavoriteUseCase,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
    private val favoriteIsExistUseCase: FavoriteIsExistUseCase
) : ViewModel() {
    private val _stations = MutableLiveData<List<StationVO>>()
    var stations: LiveData<List<StationVO>> = _stations

    private val _stationDetail = MutableLiveData<StationDetailVO>()
    var stationDetail: LiveData<StationDetailVO> = _stationDetail

    private val _favoriteState = MutableLiveData<FavoriteExistVO>()
    var favoriteState: LiveData<FavoriteExistVO> = _favoriteState

    private val _error: MutableLiveData<Event<String>> = MutableLiveData()
    val error: LiveData<Event<String>> = _error

    fun fetchAllStationAndInsert() {
        fetchAllStationAndInsertUseCase(Unit)
            .onEach { result ->
                result.mapError {
                    showError(it.toMessage())
                    it
                }
            }
            .launchIn(viewModelScope)
    }

    fun getStations(lat: Double, lon: Double) {
        getStationsUseCase(GetStationsUseCase.Parameters(lat, lon))
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stations.value = result.data
                    }

                    is Result.Error -> showError(result.exception.toMessage())
                }
            }
            .launchIn(viewModelScope)
    }

    fun fetchStationDetail(city: City, stationUid: String) {
        fetchStationDetailUseCase(FetchStationDetailUseCase.Parameters(city.apiKey, stationUid))
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _stationDetail.value = result.data
                    }

                    is Result.Error -> showError(result.exception.toMessage())
                }
            }
            .launchIn(viewModelScope)
    }

    fun favoriteIsExist(stationUid: String) {
        favoriteIsExistUseCase(FavoriteIsExistUseCase.Parameters(stationUid))
            .onEach { _favoriteState.value = FavoriteExistVO(stationUid, it.data ?: false) }
            .launchIn(viewModelScope)
    }

    fun addFavorite(stationUid: String) {
        favoriteAddUseCase(AddFavoriteUseCase.Parameters(stationUid))
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _favoriteState.value = FavoriteExistVO(stationUid, true)
                    }

                    is Result.Error -> showError(result.exception.toMessage())
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeFavorite(stationUid: String) {
        favoriteRemoveUseCase(RemoveFavoriteUseCase.Parameters(stationUid))
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _favoriteState.value = FavoriteExistVO(stationUid, false)
                    }

                    is Result.Error -> showError(result.exception.toMessage())
                }
            }
            .launchIn(viewModelScope)
    }

    private fun Exception.toMessage(): String {
        return when (this) {
            is ApiException -> this.errorMessage
            else -> this.message.orEmpty()
        }.let { message ->
            context.getString(R.string.error99999, message)
        }
    }

    private fun showError(message: String) {
        _error.value = Event(message)
    }
}