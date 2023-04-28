package com.demo.ubike.ui.main

import io.reactivex.Single
import javax.inject.Inject

class FetchFakeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Single<FakeData> {
        return homeRepository.fetchFakeData()
    }
}