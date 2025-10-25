package com.demo.ubike.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.model.vo.FavoriteStationVO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _favoriteItemClicked = MutableLiveData<FavoriteStationVO>()
    val favoriteItemClicked: LiveData<FavoriteStationVO> = _favoriteItemClicked

    fun clickFavoriteItem(vo: FavoriteStationVO) {
        _favoriteItemClicked.postValue(vo)
    }
}