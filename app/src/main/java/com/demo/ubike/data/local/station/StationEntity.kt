package com.demo.ubike.data.local.station

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.ServiceType

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
) {
    fun getServiceTypeValue(): ServiceType {
        return when (serviceType) {
            ServiceType.UBike1_0.key -> ServiceType.UBike1_0
            ServiceType.UBike2_0.key -> ServiceType.UBike2_0
            ServiceType.TBike.key -> ServiceType.TBike
            ServiceType.PBike.key -> ServiceType.PBike
            ServiceType.KBike.key -> ServiceType.KBike
            else -> ServiceType.Unknown
        }
    }
}