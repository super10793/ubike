package com.demo.ubike.data.local.favorite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavoriteEntity")
data class FavoriteEntity(
    @PrimaryKey val stationUid: String,
    val addedTimestamp: Long
)