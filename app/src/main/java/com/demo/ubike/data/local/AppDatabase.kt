package com.demo.ubike.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationDetailEntity
import com.demo.ubike.data.local.station.StationEntity

@Database(
    entities = [StationEntity::class, StationDetailEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}