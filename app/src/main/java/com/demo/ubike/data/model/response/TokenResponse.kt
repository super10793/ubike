package com.demo.ubike.data.model.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_expires_in")
    val refreshExpiresIn: Int,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("scope")
    val scope: String
)
