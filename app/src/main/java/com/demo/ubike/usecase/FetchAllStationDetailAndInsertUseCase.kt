package com.demo.ubike.usecase

import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.utils.SharePreferenceManager
import io.reactivex.Completable
import javax.inject.Inject

class FetchAllStationDetailAndInsertUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sharePrefers: SharePreferenceManager
) {
    operator fun invoke(): Completable {
        return homeRepository.fetchAllCityStationDetail(sharePrefers.token)
    }
}