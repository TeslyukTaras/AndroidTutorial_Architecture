package com.teslyuk.weatherapp.data

import com.teslyuk.weatherapp.data.model.Weather

interface IWeatherDataSource {

    fun getWeather(city: String, callback: IWeatherCallback)

    interface IWeatherCallback {
        fun onReceived(city: String, data: List<Weather>)
        fun onFailure(errorCode: Int)
    }
}