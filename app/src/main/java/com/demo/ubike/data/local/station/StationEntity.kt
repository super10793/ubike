package com.demo.ubike.data.local.station

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.ubike.data.model.City

@Entity(tableName = "StationEntity")
data class StationEntity(
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