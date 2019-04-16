package com.teslyuk.weatherapp.data.local

import androidx.room.*
import com.teslyuk.weatherapp.data.model.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}