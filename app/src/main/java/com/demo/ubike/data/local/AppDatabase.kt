package com.demo.ubike.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationEntity

@Database(entities = [StationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}