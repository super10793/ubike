package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class FavoriteIsExistUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(stationUid: String): Single<Boolean> {
        return homeRepository.checkFavoriteIsExist(stationUid)
    }
}