package com.demo.ubike.data.model.vo

import com.demo.ubike.data.model.City
import com.demo.ubike.data.model.ServiceType

data class StationVO(
    val stationUid: String,
    val stationId: String,
    val city: City,
    val serviceType: ServiceType,
    val positionLon: Double,
    val positionLat: Double,
    val stationNameZhTw: String,
    val stationNameEn: String,
    val stationAddressZhTw: String,
    val stationAddressEn: String
)
