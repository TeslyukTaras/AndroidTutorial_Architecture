package com.teslyuk.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teslyuk.weatherapp.data.model.Weather
import io.reactivex.Single

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getAll(): Single<List<Weather>>

    @Query("DELETE FROM weather")
    fun deleteAll()

    @Insert
    fun insert(weathers: List<Weather>)
}