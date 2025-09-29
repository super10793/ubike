package com.demo.ubike.usecase

import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.repository.DataStoreRepository
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import com.demo.ubike.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchStationDetailUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
    private val dataStoreRepository: DataStoreRepository
) : FlowUseCase<FetchStationDetailUseCase.Parameters, StationDetailResponse.Data>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<StationDetailResponse.Data>> = flow {
        val token = dataStoreRepository.tokensFlow.first().random()

        val result = homeRepository.fetchStationDetailByUid(
            token = token,
            cityKey = parameters.cityKey,
            stationUid = parameters.stationUid
        ).map { it.first() }

        emit(result)
    }

    data class Parameters(
        val cityKey: String,
        val stationUid: String
    )
}