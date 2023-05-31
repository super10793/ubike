package com.demo.ubike.data.local.favorite


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM FavoriteEntity")
    fun getAllFavorite(): Single<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM FavoriteEntity WHERE stationUID = :stationUid)")
    fun checkFavoriteIsExist(stationUid: String): Single<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(entity: FavoriteEntity): Completable

    @Query("DELETE FROM FavoriteEntity WHERE stationUID = :stationUid")
    fun removeFavorite(stationUid: String): Completable
}