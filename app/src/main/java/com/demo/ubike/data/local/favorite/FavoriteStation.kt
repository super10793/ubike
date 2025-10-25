package com.demo.ubike.data.local.favorite

import androidx.room.Embedded
import androidx.room.Relation
import com.demo.ubike.data.local.station.StationEntity

data class FavoriteStation(
    @Embedded val favorite: FavoriteEntity,

    @Relation(
        parentColumn = "stationUid",
        entityColumn = "stationUid"
    )
    val station: StationEntity?
)