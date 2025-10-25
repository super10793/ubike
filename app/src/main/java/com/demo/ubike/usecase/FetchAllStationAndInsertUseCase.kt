package com.demo.ubike.usecase

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.mapper.StationDataResponseMapper
import com.demo.ubike.data.model.City
import com.demo.ubike.data.repository.DataStoreRepository
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import com.demo.ubike.result.data
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchAllStationAndInsertUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val stationDataResponseMapper: StationDataResponseMapper,
) : FlowUseCase<Unit, Unit>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<Unit>> = flow {
        val tokens = dataStoreRepository.tokensFlow.first()
        if (tokens.isEmpty()) {
            emit(Result.Error(Exception("No available tokens")))
            return@flow
        }

        val cityKeys = City.entries.map { it.apiKey }

        val stations: List<StationEntity> = coroutineScope {
            cityKeys.mapIndexed { idx, apiKey ->
                async {
                    try {
                        val token = pickTokenForApi(idx, tokens)
                        homeRepository.fetchStations(token, apiKey)
                    } catch (e: Exception) {
                        Result.Error(e)
                    }
                }
            }.awaitAll()
        }.mapNotNull { it.data }.flatMap { response ->
            response.map { stationDataResponseMapper.map(it) }
        }

        if (stations.isEmpty()) {
            emit(Result.Error(Exception("No stations to insert")))
            return@flow
        }

        when (val insertResult = homeRepository.insertStationEntities(stations)) {
            is Result.Success -> emit(Result.Success(Unit))
            is Result.Error -> emit(insertResult)
        }
    }

    private fun pickTokenForApi(apiKeyIndex: Int, tokens: List<String>): String {
        return tokens[apiKeyIndex % tokens.size]
    }
}