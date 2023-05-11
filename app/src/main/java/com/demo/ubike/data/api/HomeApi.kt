package com.demo.ubike.data.api

import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.Config
import io.reactivex.Single
import retrofit2.http.*

interface HomeApi {
    @FormUrlEncoded
    @POST("/auth/realms/TDXConnect/protocol/openid-connect/token")
    fun fetchToken(
        @Field("grant_type") grantType: String = Config.GRANT_TYPE,
        @Field("client_id") clientId: String = Config.CLIENT_ID,
        @Field("client_secret") clientSecret: String = Config.CLIENT_SECRET
    ): Single<TokenResponse>
}