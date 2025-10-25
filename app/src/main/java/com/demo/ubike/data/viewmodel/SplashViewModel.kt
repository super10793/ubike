package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.ubike.result.Event
import com.demo.ubike.result.Result
import com.demo.ubike.usecase.FetchTokensUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchTokensUseCase: FetchTokensUseCase
) : ViewModel() {
    private var _fetchTokenSuccess = MutableLiveData<Event<Unit>>()
    val fetchTokenSuccess: LiveData<Event<Unit>> = _fetchTokenSuccess

    private var _fetchTokenFail = MutableLiveData<Event<Exception>>()
    val fetchTokenFail: LiveData<Event<Exception>> = _fetchTokenFail

    fun fetchTokens() {
        fetchTokensUseCase(Unit)
            .map { result ->
                when (result) {
                    is Result.Success -> _fetchTokenSuccess.value = Event(Unit)

                    is Result.Error -> _fetchTokenFail.value = Event(result.exception)
                }
            }
            .launchIn(viewModelScope)
    }
}