package com.demo.ubike.data.model.vo

import androidx.recyclerview.widget.DiffUtil
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.ServiceType

data class FavoriteStationVO(
    val stationUid: String,
    val stationId: String,
    val authorityId: String,
    val city: City,
    val serviceType: ServiceType,
    val positionLon: Double,
    val positionLat: Double,
    val stationNameZhTw: String,
    val stationNameEn: String,
    val stationAddressZhTw: String,
    val stationAddressEn: String,
    val stationDetailVO: StationDetailVO?
) {
    object Differ : DiffUtil.ItemCallback<FavoriteStationVO>() {
        override fun areItemsTheSame(
            oldItem: FavoriteStationVO,
            newItem: FavoriteStationVO
        ): Boolean {
            return oldItem.stationUid == newItem.stationUid
        }

        override fun areContentsTheSame(
            oldItem: FavoriteStationVO,
            newItem: FavoriteStationVO
        ): Boolean {
            return oldItem == newItem
        }
    }
}
