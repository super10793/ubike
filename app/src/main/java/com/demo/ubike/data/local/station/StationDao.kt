package com.demo.ubike.data.local.station


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {

    @Query("SELECT * FROM StationEntity WHERE positionLat < :maxLat AND positionLat > :minLat AND positionLon < :maxLon AND positionLon > :minLon")
    suspend fun getStationsByLocation(
        maxLat: Double,
        minLat: Double,
        maxLon: Double,
        minLon: Double
    ): List<StationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(list: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStationEntities(stationEntities: List<StationEntity>)
}