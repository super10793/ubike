package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Completable
import javax.inject.Inject

class FetchAllStationAndInsertUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Completable {
        return homeRepository.fetchAllCityStation()
    }
}