package com.demo.ubike.ui.main

import io.reactivex.Single
import javax.inject.Inject

class FetchTokenUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Single<TokenResponse> {
        return homeRepository.fetchToken()
    }
}