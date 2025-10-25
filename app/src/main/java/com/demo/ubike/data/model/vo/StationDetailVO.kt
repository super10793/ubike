package com.demo.ubike.data.model.vo

import com.demo.ubike.data.model.ServiceStatus
import com.demo.ubike.data.model.ServiceType

data class StationDetailVO(
    val stationUid: String,
    val stationId: String,
    val serviceStatus: ServiceStatus,
    val serviceType: ServiceType,
    val canRentBikes: Int,
    val canRentGeneralBikes: Int,
    val canRentElectricBikes: Int,
    val canReturnBikes: Int,
    val srcUpdateTime: String,
    val updateTime: String,
)
