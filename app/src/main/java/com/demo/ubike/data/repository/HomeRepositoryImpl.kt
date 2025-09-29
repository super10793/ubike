package com.demo.ubike.data.repository

import com.demo.ubike.Config
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.result.Result
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi,
    private val stationDao: StationDao,
    private val favoriteDao: FavoriteDao
) : HomeRepository {

    companion object {
        private const val TOKEN_PREFIX = "Bearer"

        private const val STATION_UID = "StationUID"
    }

    override suspend fun fetchToken(clientId: String, clientSecret: String): Result<TokenResponse> {
        return homeApi.fetchToken(clientId = clientId, clientSecret = clientSecret)
    }

    override suspend fun fetchStation(token: String, cityKey: String): Result<StationResponse> {
        return homeApi.fetchStation(authorization = token.withBearer(), cityKey = cityKey)
    }

    override suspend fun insertStationEntities(entities: List<StationEntity>): Result<Unit> {
        return try {
            Result.Success(stationDao.insertStationEntities(entities))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchStationDetailByUid(
        token: String,
        cityKey: String,
        stationUid: String
    ): Result<StationDetailResponse> {
        return homeApi.fetchStationDetailById(
            authorization = token.withBearer(),
            cityKey = cityKey,
            filter = stationUid.eqQuery(STATION_UID)
        )
    }

    override suspend fun getStationsByLocation(
        lat: Double,
        lon: Double
    ): Result<List<StationEntity>> {
        val maxLat = lat + Config.VISIBLE_DISTANCE_LAT
        val minLat = lat - Config.VISIBLE_DISTANCE_LAT
        val maxLon = lon + Config.VISIBLE_DISTANCE_LON
        val minLon = lon - Config.VISIBLE_DISTANCE_LON

        return try {
            Result.Success(stationDao.getStationsByLocation(maxLat, minLat, maxLon, minLon))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun checkFavoriteIsExist(stationUid: String): Result<Boolean> {
        return try {
            Result.Success(favoriteDao.checkFavoriteIsExist(stationUid))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addFavorite(entity: FavoriteEntity): Result<Unit> {
        return try {
            Result.Success(favoriteDao.insertFavorite(entity))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun removeFavorite(stationUid: String): Result<Unit> {
        return try {
            Result.Success(favoriteDao.removeFavorite(stationUid))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAllFavorite(): Result<List<FavoriteEntity>> {
        return try {
            Result.Success(favoriteDao.getAllFavorite())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun String.withBearer() = "$TOKEN_PREFIX $this"

    private fun String.eqQuery(key: String) = "$key eq '$this'"
}