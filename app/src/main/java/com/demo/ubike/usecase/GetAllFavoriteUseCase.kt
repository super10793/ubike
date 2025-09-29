package com.demo.ubike.usecase

import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllFavoriteUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository
) : FlowUseCase<Unit, List<FavoriteEntity>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<FavoriteEntity>>> = flow {
        emit(homeRepository.getAllFavorite())
    }
}