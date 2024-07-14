package com.demo.ubike.usecase

import com.demo.ubike.data.model.City
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.utils.SharePreferenceManager
import io.reactivex.Completable
import javax.inject.Inject

class FetchStationAndInsertUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sharePrefers: SharePreferenceManager
) {
    operator fun invoke(city: City): Completable {
        return homeRepository.fetchStationAndInsert(sharePrefers.randomToken(), city)
    }
}