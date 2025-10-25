package com.demo.ubike.ui.fragment

import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.vo.FavoriteStationVO

interface OnFavoriteItemClickListener {

    fun onFavoriteContentClick(favoriteStationVO: FavoriteStationVO)

    fun onFavoriteRemoveClick(stationUid: String, stationName: String)

    fun onGoToGoogleMapClick(lat: Double, lon: Double)

    fun onRefresh(stationUid: String, city: City)
}