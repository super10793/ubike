package com.demo.ubike.data.repository

import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.favorite.FavoriteStation
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.response.StationDetailListResponse
import com.demo.ubike.data.model.response.StationListResponse
import com.demo.ubike.data.model.response.TokenResponse
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

        private const val VISIBLE_DISTANCE_LAT = 0.02

        private const val VISIBLE_DISTANCE_LON = 0.017
    }

    override suspend fun fetchToken(clientId: String, clientSecret: String): Result<TokenResponse> {
        return homeApi.fetchToken(clientId = clientId, clientSecret = clientSecret)
    }

    override suspend fun fetchStations(
        token: String,
        cityKey: String
    ): Result<StationListResponse> {
        return homeApi.fetchStations(authorization = token.withBearer(), cityKey = cityKey)
    }

    override suspend fun insertStationEntities(entities: List<StationEntity>): Result<Unit> {
        return try {
            Result.Success(stationDao.insertStationEntities(entities))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun fetchStationDetailsByUid(
        token: String,
        cityKey: String,
        stationUid: String
    ): Result<StationDetailListResponse> {
        return homeApi.fetchStationDetails(
            authorization = token.withBearer(),
            cityKey = cityKey,
            filter = stationUid.eqQuery(STATION_UID)
        )
    }

    override suspend fun getStationsByLocation(
        lat: Double,
        lon: Double
    ): Result<List<StationEntity>> {
        val maxLat = lat + VISIBLE_DISTANCE_LAT
        val minLat = lat - VISIBLE_DISTANCE_LAT
        val maxLon = lon + VISIBLE_DISTANCE_LON
        val minLon = lon - VISIBLE_DISTANCE_LON

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

    override suspend fun addFavorite(stationUid: String): Result<Unit> {
        return try {
            val entity = FavoriteEntity(
                stationUid = stationUid,
                addedTimestamp = System.currentTimeMillis()
            )
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

    override suspend fun getFavoriteStations(): Result<List<FavoriteStation>> {
        return try {
            Result.Success(favoriteDao.getFavoriteStations())
        } catch (e: Exception) {
            Result.Success(emptyList())
        }
    }

    private fun String.withBearer() = "$TOKEN_PREFIX $this"

    private fun String.eqQuery(key: String) = "$key eq '$this'"
}