package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.di.IoDispatcher
import com.demo.ubike.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FavoriteIsExistUseCase @Inject constructor(
    @IoDispatcher dispatcher: CoroutineDispatcher,
    private val homeRepository: HomeRepository
) : FlowUseCase<FavoriteIsExistUseCase.Parameters, Boolean>(dispatcher) {

    override fun execute(parameters: Parameters): Flow<Result<Boolean>> = flow {
        emit(homeRepository.checkFavoriteIsExist(parameters.stationUid))
    }

    data class Parameters(
        val stationUid: String
    )
}