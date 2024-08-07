package com.demo.ubike.usecase

import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.repository.HomeRepository
import com.demo.ubike.utils.SharePreferenceManager
import io.reactivex.Single
import javax.inject.Inject

class FetchStationDetailUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val sharePrefers: SharePreferenceManager
) {
    operator fun invoke(city: City): Single<StationDetailResponse> {
        return homeRepository.fetchStationDetail(sharePrefers.randomToken(), city)
    }

    fun byStationUid(city: City, stationUID: String): Single<StationDetailResponse> {
        return homeRepository.fetchStationDetailById(
            sharePrefers.randomToken(),
            city,
            stationUID
        )
    }
}