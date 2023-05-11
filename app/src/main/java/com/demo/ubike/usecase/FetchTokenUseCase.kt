package com.demo.ubike.usecase

import com.demo.ubike.data.model.TokenResponse
import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class FetchTokenUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Single<TokenResponse> {
        return homeRepository.fetchToken()
    }
}