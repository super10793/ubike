package com.demo.ubike.ui.main

data class TokenResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_expires_in: Int,
    val token_type: String,
    val scope: String
)
