package com.demo.ubike.usecase

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository
) : FlowUseCase<GetStationsUseCase.Parameters, List<StationEntity>>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<List<StationEntity>>> = flow {
        emit(homeRepository.getStationsByLocation(parameters.lat, parameters.lon))
    }

    data class Parameters(
        val lat: Double,
        val lon: Double
    )
}