package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository
) : FlowUseCase<RemoveFavoriteUseCase.Parameters, Unit>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<Unit>> = flow {
        emit(homeRepository.removeFavorite(parameters.stationUid))
    }

    data class Parameters(
        val stationUid: String
    )
}