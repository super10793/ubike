package com.demo.ubike.data.repository

import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import io.reactivex.Completable
import io.reactivex.Single

interface HomeRepository {
    fun fetchToken(): Single<TokenResponse>

    fun fetchStation(token: String, city: City): Single<StationResponse>

    fun fetchStationAndInsert(token: String, city: City): Completable

    fun fetchStationDetail(token: String, city: City): Single<StationDetailResponse>
}