package com.teslyuk.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teslyuk.weatherapp.data.model.Weather

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getAll(): List<Weather>

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Insert
    fun insert(weathers: List<Weather>)
}