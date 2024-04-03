package com.example.appmeteo.android
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CityDao {
    @Query("SELECT * FROM city LIMIT 1")
    suspend fun getCity(): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)
}
