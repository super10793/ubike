package com.demo.ubike.data.local.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.ServiceType

@Entity(tableName = "FavoriteEntity")
data class FavoriteEntity(
    @PrimaryKey val stationUID: String,
    val stationID: String,
    val authorityID: String,
    val city: City,
    val bikesCapacity: Int,
    val serviceType: Int,
    val positionLon: Double,
    val positionLat: Double,
    val stationNameZhTw: String,
    val stationNameEn: String,
    val stationAddressZhTw: String,
    val stationAddressEn: String
)