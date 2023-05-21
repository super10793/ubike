package com.demo.ubike.data.local.station


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface StationDao {
    @Query("SELECT * FROM StationEntity WHERE positionLat < :maxLat AND positionLat > :minLat AND positionLon < :maxLon AND positionLon > :minLon")
    fun getStationsByLocation(
        maxLat: Double,
        minLat: Double,
        maxLon: Double,
        minLon: Double
    ): Observable<List<StationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStation(list: List<StationEntity>): Completable
}