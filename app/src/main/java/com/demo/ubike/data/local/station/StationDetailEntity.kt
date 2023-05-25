package com.demo.ubike.data.local.station

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.ubike.data.model.City

@Entity(tableName = "StationDetailEntity")
data class StationDetailEntity(
    @PrimaryKey val stationUID: String,
    val stationID: String,
    val city: City,
    val serviceStatus: Int,
    val serviceType: Int,
    val availableRentBikes: Int,
    val availableRentGeneralBikes: Int,
    val availableRentElectricBikes: Int,
    val availableReturnBikes: Int,
    val srcUpdateTime: String,
    val updateTime: String,
)