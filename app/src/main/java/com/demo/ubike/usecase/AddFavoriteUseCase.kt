package com.demo.ubike.usecase

import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.repository.HomeRepository
import io.reactivex.Completable
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(entity: FavoriteEntity): Completable {
        return homeRepository.addFavorite(entity)
    }
}