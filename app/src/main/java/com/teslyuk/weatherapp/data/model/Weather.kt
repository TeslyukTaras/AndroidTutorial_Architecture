package com.teslyuk.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Weather {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var time: Long = 0
    var temp: Double = 0.0
    var tempMin: Double = 0.0
    var tempMax: Double = 0.0
    var humidity: Int = 0
    var description: String? = null
    var icon: String? = null

    constructor()

    constructor(info: WeatherCut) {
        time = info.dt
        temp = info.main.temp
        tempMin = info.main.tempMin
        tempMax = info.main.tempMax
        humidity = info.main.humidity
        if (info.shortInto != null && info.shortInto!!.isNotEmpty()) {
            description = info.shortInto!![0].description
            icon = info.shortInto!![0].icon
        }
    }
}