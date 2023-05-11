package com.demo.ubike.data.repository

import com.demo.ubike.data.model.TokenResponse
import io.reactivex.Single

interface HomeRepository {
    fun fetchToken(): Single<TokenResponse>
}