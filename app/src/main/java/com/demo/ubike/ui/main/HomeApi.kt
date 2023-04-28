package com.demo.ubike.ui.main

import io.reactivex.Single
import retrofit2.http.GET

interface HomeApi {
    @GET("/todos/1")
    fun fetchFakeData(): Single<FakeData>
}