package com.demo.ubike.usecase

import com.demo.ubike.data.mapper.StationDetailDataResponseMapper
import com.demo.ubike.data.model.vo.StationDetailVO
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
    private val dataStoreRepository: DataStoreRepository,
    private val stationDetailDataResponseMapper: StationDetailDataResponseMapper,
) : FlowUseCase<FetchStationDetailUseCase.Parameters, StationDetailVO>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<StationDetailVO>> = flow {
        val token = dataStoreRepository.tokensFlow.first().random()

        val response = homeRepository.fetchStationDetailsByUid(
            token = token,
            cityKey = parameters.cityKey,
            stationUid = parameters.stationUid
        )

        val result = response.map {
            stationDetailDataResponseMapper.map(it.first())
        }

        emit(result)
    }

    data class Parameters(
        val cityKey: String,
        val stationUid: String
    )
}