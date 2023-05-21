package com.demo.ubike.data.repository

import android.content.Context
import com.demo.ubike.Config
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationEntity
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.utils.SharePreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharePrefer: SharePreferenceManager,
    private val homeApi: HomeApi,
    private val stationDao: StationDao
) : HomeRepository {
    override fun fetchToken(): Single<TokenResponse> {
        return homeApi.fetchToken()
    }

    override fun fetchStation(token: String, city: City): Single<StationResponse> {
        val url = String.format(Config.API_STATION_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStation(url, tokenStr)
    }

    override fun fetchStationAndInsert(token: String, city: City): Completable {
        val url = String.format(Config.API_STATION_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStation(url, tokenStr)
            .flatMapCompletable {
                val list = it.map { response ->
                    StationEntity(
                        stationUID = response.stationUID,
                        stationID = response.stationID,
                        authorityID = response.authorityID,
                        cityName = city.apiKey,
                        bikesCapacity = response.bikesCapacity,
                        serviceType = response.serviceType,
                        positionLon = response.stationPosition.positionLon,
                        positionLat = response.stationPosition.positionLat,
                        stationNameZhTw = response.stationName.zh_tw,
                        stationNameEn = response.stationName.en,
                        stationAddressZhTw = response.stationAddress.zh_tw,
                        stationAddressEn = response.stationAddress.en
                    )
                }
                stationDao.insertStation(list)
            }
    }

    override fun fetchStationDetail(token: String, city: City): Single<StationDetailResponse> {
        val url = String.format(Config.API_STATION_DETAIL_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStationDetail(url, tokenStr)
    }

    override fun getStationsByLocation(lat: Double, lon: Double): Observable<List<StationEntity>> {
        val maxLat = lat + 0.02
        val minLat = lat - 0.02
        val maxLon = lon + 0.017
        val minLon = lon - 0.017
        return stationDao.getStationsByLocation(maxLat, minLat, maxLon, minLon)
    }
}