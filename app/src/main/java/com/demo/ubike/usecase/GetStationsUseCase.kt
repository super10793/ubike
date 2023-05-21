package com.demo.ubike.usecase

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(
        lat: Double,
        lon: Double
    ): Observable<List<StationEntity>> {
        return homeRepository.getStationsByLocation(lat, lon)
    }
}