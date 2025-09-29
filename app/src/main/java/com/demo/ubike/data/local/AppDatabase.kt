package com.demo.ubike.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.ubike.data.local.favorite.FavoriteDao
import com.demo.ubike.data.local.favorite.FavoriteEntity
import com.demo.ubike.data.local.station.StationDao
import com.demo.ubike.data.local.station.StationDetailEntity
import com.demo.ubike.data.local.station.StationEntity

@Database(
    entities = [
        StationEntity::class,
        StationDetailEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {

        private const val DATABASE_NAME = "app-database"

        fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}