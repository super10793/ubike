package com.demo.ubike.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.ubike.R
import com.demo.ubike.data.model.vo.FavoriteStationVO
import com.demo.ubike.data.model.vo.StationDetailVO
import com.demo.ubike.network.ApiException
import com.demo.ubike.result.Event
import com.demo.ubike.result.Result
import com.demo.ubike.result.map
import com.demo.ubike.result.succeeded
import com.demo.ubike.usecase.FetchStationDetailUseCase
import com.demo.ubike.usecase.GetFavoriteStationsUseCase
import com.demo.ubike.usecase.RemoveFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val favoriteRemoveUseCase: RemoveFavoriteUseCase,
    private val getFavoriteStationsUseCase: GetFavoriteStationsUseCase,
    private val fetchStationDetailUseCase: FetchStationDetailUseCase
) : ViewModel() {

    private val _favoriteStations = MutableLiveData<List<FavoriteStationVO>>()
    val favoriteStations: LiveData<List<FavoriteStationVO>> = _favoriteStations

    private val _favoriteRemoved = MutableLiveData<Event<Boolean>>()
    val favoriteRemoved: LiveData<Event<Boolean>> = _favoriteRemoved

    private val _stationDetail = MutableSharedFlow<StationDetailVO>()
    val stationDetail = _stationDetail.asSharedFlow()

    private val pendingDetails: MutableList<StationDetailVO> = mutableListOf()


    private val _error: MutableLiveData<Event<String>> = MutableLiveData()
    val error: LiveData<Event<String>> = _error

    init {
        debounceStationDetailUpdate()
    }

    private fun debounceStationDetailUpdate() {
        stationDetail
            .onEach { pendingDetails.add(it) }
            .debounce(500L)
            .onEach { flushPendingDetails() }
            .launchIn(viewModelScope)
    }

    private fun flushPendingDetails() {
        if (pendingDetails.isEmpty()) return
        val current = favoriteStations.value?.toMutableList() ?: return
        pendingDetails.forEach { detail ->
            val index = current.indexOfFirst { it.stationUid == detail.stationUid }
            if (index != -1) {
                current[index] = current[index].copy(stationDetailVO = detail)
            }
        }
        _favoriteStations.value = current
        pendingDetails.clear()
    }

    fun getFavoriteStations() {
        getFavoriteStationsUseCase(Unit)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _favoriteStations.value = result.data
                    }

                    is Result.Error -> showError(result.exception.toMessage())
                }
            }
            .launchIn(viewModelScope)
    }

    fun fetchStationDetail(stationUid: String, cityKey: String) {
        val parameters =
            FetchStationDetailUseCase.Parameters(cityKey = cityKey, stationUid = stationUid)
        fetchStationDetailUseCase(parameters)
            .onEach { result -> result.map { _stationDetail.emit(it) } }
            .launchIn(viewModelScope)
    }

    fun removeFavorite(stationUid: String) {
        val parameters = RemoveFavoriteUseCase.Parameters(stationUid)
        favoriteRemoveUseCase(parameters)
            .onEach { _favoriteRemoved.value = Event(it.succeeded) }
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