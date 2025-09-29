package com.demo.ubike.data.local.favorite


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM FavoriteEntity")
    suspend fun getAllFavorite(): List<FavoriteEntity>

    @Query("SELECT EXISTS (SELECT 1 FROM FavoriteEntity WHERE stationUID = :stationUid)")
    suspend fun checkFavoriteIsExist(stationUid: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(entity: FavoriteEntity)

    @Query("DELETE FROM FavoriteEntity WHERE stationUID = :stationUid")
    suspend fun removeFavorite(stationUid: String)
}