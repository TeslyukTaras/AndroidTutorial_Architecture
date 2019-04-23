package com.teslyuk.weatherapp.data

import com.teslyuk.weatherapp.data.model.Weather
import io.reactivex.Single

interface IWeatherDataSource {

    fun getWeather(city: String): Single<WeatherData>

    class WeatherData(var city: String, var data: List<Weather>)
}