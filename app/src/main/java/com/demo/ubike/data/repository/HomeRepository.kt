package com.demo.ubike.data.repository

import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.result.Result

interface HomeRepository {

    suspend fun fetchToken(clientId: String, clientSecret: String): Result<TokenResponse>

    suspend fun fetchStation(token: String, cityKey: String): Result<StationResponse>

    suspend fun insertStationEntities(entities: List<StationEntity>): Result<Unit>

    suspend fun fetchStationDetailByUid(
        token: String,
        cityKey: String,
        stationUid: String
    ): Result<StationDetailResponse>

    suspend fun getStationsByLocation(lat: Double, lon: Double): Result<List<StationEntity>>

    suspend fun checkFavoriteIsExist(stationUid: String): Result<Boolean>

    suspend fun addFavorite(entity: FavoriteEntity): Result<Unit>

    suspend fun removeFavorite(stationUid: String): Result<Unit>

    suspend fun getAllFavorite(): Result<List<FavoriteEntity>>
}