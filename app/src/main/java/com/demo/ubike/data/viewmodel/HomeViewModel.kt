package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.ubike.data.local.favorite.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {
    private val _favoriteItemClicked = MutableLiveData<FavoriteEntity>()
    val favoriteItemClicked: LiveData<FavoriteEntity> = _favoriteItemClicked

    fun clickFavoriteItem(entity: FavoriteEntity) {
        _favoriteItemClicked.postValue(entity)
    }
}