package com.demo.ubike.data.local.station

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.ubike.data.model.City

@Entity(tableName = "StationEntity")
data class StationEntity(
    @PrimaryKey val stationUid: String,
    val stationId: String,
    val authorityId: String,
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