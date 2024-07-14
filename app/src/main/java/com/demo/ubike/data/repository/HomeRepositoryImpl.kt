package com.demo.ubike.data.repository

import android.content.Context
import com.demo.ubike.Config
import com.demo.ubike.R
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.favorite.FavoriteEntity
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
    private val stationDao: StationDao,
    private val favoriteDao: FavoriteDao
) : HomeRepository {
    override fun fetchToken(): Single<TokenResponse> {
        return homeApi.fetchToken(
            clientId = Config.CLIENT_ID_1,
            clientSecret = Config.CLIENT_SECRET_1
        )
    }

    override fun fetchTokens(): Single<List<TokenResponse>> {

        val tokenRequests = listOf(
            homeApi.fetchToken(
                clientId = Config.CLIENT_ID_1, clientSecret = Config.CLIENT_SECRET_1
            ),
            homeApi.fetchToken(
                clientId = Config.CLIENT_ID_2, clientSecret = Config.CLIENT_SECRET_2
            ),
            homeApi.fetchToken(
                clientId = Config.CLIENT_ID_3, clientSecret = Config.CLIENT_SECRET_3
            )
        )

        return Observable.fromIterable(tokenRequests)
            .concatMapSingle { api ->
                api.map { it }
            }
            .toList()
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

    override fun fetchAllCityStation(): Completable {
        val requests: List<Single<List<StationResponse.Data>>> = listOf(
            fetchPart1CityStation(),
            fetchPart2CityStation(),
            fetchPart3CityStation(),
        )

        return Single.zip(requests) { results ->
            results.flatMap { it as List<*> }
        }
            .flatMapCompletable { allStations ->
                val insert = allStations.mapNotNull { response ->
                    (response as? StationResponse.Data)?.let {
                        StationEntity(
                            stationUID = response.stationUID,
                            stationID = response.stationID,
                            authorityID = response.authorityID,
                            city = response.city,
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
                }
                stationDao.insertStation(insert)
            }
            .onErrorResumeNext { throwable ->
                Completable.error(throwable)
            }
    }

    override fun fetchPart1CityStation(): Single<List<StationResponse.Data>> {
        return fetchPartOfCityStations(City.part1Cities(), sharePrefer.tokens.toList()[0])
    }

    override fun fetchPart2CityStation(): Single<List<StationResponse.Data>> {
        return fetchPartOfCityStations(City.part2Cities(), sharePrefer.tokens.toList()[1])
    }

    override fun fetchPart3CityStation(): Single<List<StationResponse.Data>> {
        return fetchPartOfCityStations(City.part3Cities(), sharePrefer.tokens.toList()[2])
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
        val maxLat = lat + Config.VISIBLE_DISTANCE_LAT
        val minLat = lat - Config.VISIBLE_DISTANCE_LAT
        val maxLon = lon + Config.VISIBLE_DISTANCE_LON
        val minLon = lon - Config.VISIBLE_DISTANCE_LON
        return stationDao.getStationsByLocation(maxLat, minLat, maxLon, minLon)
    }

    override fun checkFavoriteIsExist(stationUid: String): Single<Boolean> {
        return favoriteDao.checkFavoriteIsExist(stationUid)
    }

    override fun addFavorite(entity: FavoriteEntity): Completable {
        return favoriteDao.insertFavorite(entity)
    }

    override fun removeFavorite(stationUid: String): Completable {
        return favoriteDao.removeFavorite(stationUid)
    }

    override fun getAllFavorite(): Single<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorite()
    }

    /* private functions */

    private fun fetchPartOfCityStations(
        cities: List<City>,
        token: String
    ): Single<List<StationResponse.Data>> {
        val apis = cities.map { city ->
            val url = String.format(Config.API_STATION_URL, city.apiKey)
            val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
            homeApi.fetchStation(url, tokenStr).map { response ->
                response.onEach { it.city = city }
            }
        }

        return Single.zip(apis) { results ->
            val lists = results.mapNotNull {
                (it as? (StationResponse))?.toList()
            }
            lists.flatten()
        }.map { it }
    }
}