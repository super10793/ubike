package com.demo.ubike.data.repository

import android.content.Context
import com.demo.ubike.Config
import com.demo.ubike.R
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationDetailEntity
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
                        city = city,
                        bikesCapacity = response.bikesCapacity,
                        serviceType = response.serviceType,
                        positionLon = response.stationPosition.positionLon,
                        positionLat = response.stationPosition.positionLat,
                        stationNameZhTw = response.stationName.zh_tw,
                        stationNameEn = response.stationName.en.orEmpty(),
                        stationAddressZhTw = response.stationAddress.zh_tw.orEmpty(),
                        stationAddressEn = response.stationAddress.en.orEmpty()
                    )
                }
                stationDao.insertStation(list)
            }
    }

    override fun fetchAllCityStation(token: String): Completable {
        val list = City.values().toMutableList()
        return Observable.fromIterable(list)
            .flatMapCompletable { item ->
                val url = String.format(Config.API_STATION_URL, item.apiKey)
                val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
                homeApi.fetchStation(url, tokenStr)
                    .flatMapCompletable {
                        val insert = it.map { response ->
                            StationEntity(
                                stationUID = response.stationUID,
                                stationID = response.stationID,
                                authorityID = response.authorityID,
                                city = item,
                                bikesCapacity = response.bikesCapacity,
                                serviceType = response.serviceType,
                                positionLon = response.stationPosition.positionLon,
                                positionLat = response.stationPosition.positionLat,
                                stationNameZhTw = response.stationName.zh_tw,
                                stationNameEn = response.stationName.en.orEmpty(),
                                stationAddressZhTw = response.stationAddress.zh_tw.orEmpty(),
                                stationAddressEn = response.stationAddress.en.orEmpty()
                            )
                        }
                        stationDao.insertStation(insert)
                    }
                    .onErrorResumeNext { throwable ->
                        Completable.error(throwable)
                    }
                    .toObservable<Void>()
                    .onErrorResumeNext(Observable.empty())
                    .ignoreElements()
            }
    }

    override fun fetchAllCityStationDetail(token: String): Completable {
        val list = City.values().toMutableList()
        return Observable.fromIterable(list)
            .flatMapCompletable { item ->
                val url = String.format(Config.API_STATION_DETAIL_URL, item.apiKey)
                val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
                homeApi.fetchStationDetail(url, tokenStr)
                    .flatMapCompletable {
                        val insert = it.map { response ->
                            StationDetailEntity(
                                stationUID = response.stationUID,
                                stationID = response.stationID,
                                city = item,
                                serviceStatus = response.serviceStatus,
                                serviceType = response.serviceType,
                                availableRentBikes = response.availableRentBikes,
                                availableRentGeneralBikes = response.availableRentBikesDetail.generalBikes,
                                availableRentElectricBikes = response.availableRentBikesDetail.electricBikes,
                                availableReturnBikes = response.availableReturnBikes,
                                srcUpdateTime = response.srcUpdateTime,
                                updateTime = response.updateTime
                            )
                        }
                        stationDao.insertStationDetail(insert)
                    }
                    .onErrorResumeNext { throwable ->
                        Completable.error(throwable)
                    }
                    .toObservable<Void>()
                    .onErrorResumeNext(Observable.empty())
                    .ignoreElements()
            }
    }

    override fun fetchStationDetail(token: String, city: City): Single<StationDetailResponse> {
        val url = String.format(Config.API_STATION_DETAIL_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStationDetail(url, tokenStr)
    }

    override fun fetchStationDetailById(
        token: String,
        city: City,
        stationUid: String
    ): Single<StationDetailResponse> {
        val url = String.format(Config.API_STATION_DETAIL_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        val filterStr = context.getString(R.string.api_filter_station_uid, stationUid)
        return homeApi.fetchStationDetailById(
            authorization = tokenStr,
            url = url,
            filter = filterStr
        )
    }

    override fun getStationsByLocation(lat: Double, lon: Double): Observable<List<StationEntity>> {
        val maxLat = lat + 0.02
        val minLat = lat - 0.02
        val maxLon = lon + 0.017
        val minLon = lon - 0.017
        return stationDao.getStationsByLocation(maxLat, minLat, maxLon, minLon)
    }
}