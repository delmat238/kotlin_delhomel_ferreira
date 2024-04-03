package com.example.appmeteo.android
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class CityEntity(
    @PrimaryKey val id: Int = 0,
    val cityName: String
)
