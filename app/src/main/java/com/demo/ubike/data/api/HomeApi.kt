package com.demo.ubike.data.api

import com.demo.ubike.Config
import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import io.reactivex.Single
import retrofit2.http.*

interface HomeApi {
    /**
     * TDX會員可至【會員專區-資料服務-服務金鑰】功能頁面
     * 從預設金鑰(或建立新的金鑰)取得Client Id和Client Secret資訊
     * 分別輸入至下方client_id和client_secret欄位
     * Token URL: https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token
     * */
    @FormUrlEncoded
    @POST("/auth/realms/TDXConnect/protocol/openid-connect/token")
    fun fetchToken(
        @Field("grant_type") grantType: String = Config.GRANT_TYPE,
        @Field("client_id") clientId: String = Config.CLIENT_ID,
        @Field("client_secret") clientSecret: String = Config.CLIENT_SECRET
    ): Single<TokenResponse>

    /**
     * * 取得指定[縣市]的公共自行車租借站位資料
     * */
    @GET
    fun fetchStation(
        @Url url: String,
        @Header("authorization") authorization: String,
        @Query("\$format") format: String = Config.API_FORMAT,
    ): Single<StationResponse>

    /**
     * * 取得動態指定[縣市]的公共自行車即時車位資料
     * */
    @GET
    fun fetchStationDetail(
        @Url url: String,
        @Header("authorization") authorization: String,
        @Query("\$format") format: String = Config.API_FORMAT,
    ): Single<StationDetailResponse>

    /**
     * * 取得動態指定[縣市]的公共自行車即時車位資料
     * */
    @GET
    fun fetchStationDetailById(
        @Url url: String,
        @Header("authorization") authorization: String,
        @Query("\$format") format: String = Config.API_FORMAT,
        @Query("\$filter") filter: String,
    ): Single<StationDetailResponse>
}