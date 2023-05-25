package com.demo.ubike.data.repository

import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface HomeRepository {
    fun fetchToken(): Single<TokenResponse>

    fun fetchStation(token: String, city: City): Single<StationResponse>

    fun fetchStationAndInsert(token: String, city: City): Completable

    fun fetchAllCityStation(token: String): Completable

    fun fetchAllCityStationDetail(token: String): Completable

    fun fetchStationDetail(token: String, city: City): Single<StationDetailResponse>

    fun fetchStationDetailById(
        token: String,
        city: City,
        stationUid: String
    ): Single<StationDetailResponse>

    fun getStationsByLocation(lat: Double, lon: Double): Observable<List<StationEntity>>
}