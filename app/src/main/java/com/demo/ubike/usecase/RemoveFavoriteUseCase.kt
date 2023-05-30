package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Completable
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(stationUid: String): Completable {
        return homeRepository.removeFavorite(stationUid)
    }
}