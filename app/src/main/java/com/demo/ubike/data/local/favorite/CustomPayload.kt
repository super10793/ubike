package com.demo.ubike.data.local.favorite

data class CustomPayload(
    val stationUid: String? = null,
    val serviceStatus: Int? = null,
    val availableRentBikes: Int? = null,
    val availableReturnBikes: Int? = null,
    val availableRentGeneralBikes: Int? = null,
    val availableRentElectricBikes: Int? = null,
    val updateTime: String? = null
)
