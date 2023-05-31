package com.demo.ubike.usecase

import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAllFavoriteUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Single<List<FavoriteEntity>> {
        return homeRepository.getAllFavorite()
    }
}