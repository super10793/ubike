package com.demo.ubike.usecase

import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.utils.SharePreferenceManager
import io.reactivex.Single
import javax.inject.Inject

class FetchStationUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sharePrefers: SharePreferenceManager
) {
    operator fun invoke(city: City): Single<StationResponse> {
        return homeRepository.fetchStation(sharePrefers.token, city)
    }
}