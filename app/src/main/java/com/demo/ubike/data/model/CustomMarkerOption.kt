package com.demo.ubike.data.model

import com.demo.ubike.data.local.station.StationEntity
import com.google.android.gms.maps.model.MarkerOptions

data class CustomMarkerOption(
    var id: String,
    var markerOptions: MarkerOptions,
    val stationEntity: StationEntity
)
