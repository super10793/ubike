package com.demo.ubike.ui.main

import io.reactivex.Single
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {
    override fun fetchToken(): Single<TokenResponse> {
        return homeApi.fetchToken()
    }
}