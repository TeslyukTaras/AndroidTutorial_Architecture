package com.teslyuk.weatherapp

import android.app.Application
import androidx.room.Room
import com.teslyuk.weatherapp.data.local.AppDatabase


class WeatherApp : Application() {

    lateinit var db: AppDatabase

    companion object {
        var instance: WeatherApp? = null
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        )
            .allowMainThreadQueries()
            .build()
        instance = this
    }
}