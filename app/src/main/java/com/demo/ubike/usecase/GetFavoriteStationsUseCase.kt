package com.demo.ubike.usecase

import com.demo.ubike.data.mapper.FavoriteStationEntityMapper
import com.demo.ubike.data.model.vo.FavoriteStationVO
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import com.demo.ubike.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFavoriteStationsUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository,
    private val favoriteStationEntityMapper: FavoriteStationEntityMapper
) : FlowUseCase<Unit, List<FavoriteStationVO>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<FavoriteStationVO>>> = flow {
        emit(homeRepository.getFavoriteStations().map {
            favoriteStationEntityMapper.map(it)
        })
    }
}