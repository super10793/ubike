package com.demo.ubike.data.repository

import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.favorite.FavoriteStation
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.response.StationDetailListResponse
import com.demo.ubike.data.model.response.StationListResponse
import com.demo.ubike.data.model.response.TokenResponse
import com.demo.ubike.result.Result

interface HomeRepository {

    suspend fun fetchToken(clientId: String, clientSecret: String): Result<TokenResponse>

    suspend fun fetchStations(token: String, cityKey: String): Result<StationListResponse>

    suspend fun insertStationEntities(entities: List<StationEntity>): Result<Unit>

    suspend fun fetchStationDetailsByUid(
        token: String,
        cityKey: String,
        stationUid: String
    ): Result<StationDetailListResponse>

    suspend fun getStationsByLocation(lat: Double, lon: Double): Result<List<StationEntity>>

    suspend fun checkFavoriteIsExist(stationUid: String): Result<Boolean>

    suspend fun addFavorite(stationUid: String): Result<Unit>

    suspend fun removeFavorite(stationUid: String): Result<Unit>

    suspend fun getAllFavorite(): Result<List<FavoriteEntity>>

    suspend fun getFavoriteStations(): Result<List<FavoriteStation>>
}