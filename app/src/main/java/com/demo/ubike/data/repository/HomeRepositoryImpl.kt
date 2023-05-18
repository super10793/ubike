package com.demo.ubike.data.repository

import android.content.Context
import com.demo.ubike.Config
import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Single
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val homeApi: HomeApi,
) : HomeRepository {
    override fun fetchToken(): Single<TokenResponse> {
        return homeApi.fetchToken()
    }

    override fun fetchStation(token: String, city: City): Single<StationResponse> {
        val url = String.format(Config.API_STATION_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStation(url, tokenStr)
    }

    override fun fetchStationDetail(token: String, city: City): Single<StationDetailResponse> {
        val url = String.format(Config.API_STATION_DETAIL_URL, city.apiKey)
        val tokenStr = String.format(Config.API_HEADER_TOKEN, token)
        return homeApi.fetchStationDetail(url, tokenStr)
    }
}