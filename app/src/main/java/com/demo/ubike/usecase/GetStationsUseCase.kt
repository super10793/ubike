package com.demo.ubike.usecase

import com.demo.ubike.data.mapper.StationEntityMapper
import com.demo.ubike.data.model.vo.StationVO
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import com.demo.ubike.result.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
    private val stationEntityMapper: StationEntityMapper
) : FlowUseCase<GetStationsUseCase.Parameters, List<StationVO>>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<List<StationVO>>> = flow {
        val result = homeRepository.getStationsByLocation(parameters.lat, parameters.lon)
        val stationEntities = result.data ?: emptyList()
        val list = stationEntities.map { stationEntityMapper.map(it) }
        emit(Result.Success(list))
    }

    data class Parameters(
        val lat: Double,
        val lon: Double
    )
}