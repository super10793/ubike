package com.demo.ubike.data.repository

import com.demo.ubike.data.api.HomeApi
import com.demo.ubike.data.model.TokenResponse
import io.reactivex.Single
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeApi: HomeApi
) : HomeRepository {
    override fun fetchToken(): Single<TokenResponse> {
        return homeApi.fetchToken()
    }
}