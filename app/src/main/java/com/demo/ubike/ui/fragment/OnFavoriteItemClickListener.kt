package com.demo.ubike.ui.fragment

import com.demo.ubike.data.local.favorite.FavoriteEntity

interface OnFavoriteItemClickListener {
    fun onFavoriteContentClick(entity: FavoriteEntity)
    fun onFavoriteRemoveClick(stationUid: String, stationName: String)
    fun onGoToGoogleMapClick(lat: Double, lon: Double)
}