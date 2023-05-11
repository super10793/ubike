package com.demo.ubike.ui.main

import io.reactivex.Single

interface HomeRepository {
    fun fetchToken(): Single<TokenResponse>
}