package com.demo.ubike.data.api

import com.demo.ubike.data.model.StationDetailResponse
import com.demo.ubike.data.model.StationResponse
import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.result.Result
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {

    companion object {
        private const val GRANT_TYPE = "client_credentials"

        private const val FORMAT = "JSON"
    }

    /**
     * TDX會員可至【會員專區-資料服務-服務金鑰】功能頁面
     * 從預設金鑰(或建立新的金鑰)取得Client Id和Client Secret資訊
     * 分別輸入至下方client_id和client_secret欄位
     * Token URL: https://tdx.transportdata.tw/auth/realms/TDXConnect/protocol/openid-connect/token
     * */
    @FormUrlEncoded
    @POST("/auth/realms/TDXConnect/protocol/openid-connect/token")
    suspend fun fetchToken(
        @Field("grant_type") grantType: String = GRANT_TYPE,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Result<TokenResponse>

    /**
     * * 取得指定[縣市]的公共自行車租借站位資料
     * */
    @GET("/api/basic/v2/Bike/Station/City/{cityKey}")
    suspend fun fetchStation(
        @Header("authorization") authorization: String,
        @Path("cityKey") cityKey: String,
        @Query("\$format") format: String = FORMAT,
    ): Result<StationResponse>

    /**
     * * 取得動態指定[縣市]的公共自行車即時車位資料
     * */
    @GET("/api/basic/v2/Bike/Availability/City/{cityKey}")
    suspend fun fetchStationDetailById(
        @Header("authorization") authorization: String,
        @Path("cityKey") cityKey: String,
        @Query("\$filter") filter: String,
        @Query("\$format") format: String = FORMAT,
    ): Result<StationDetailResponse>
}