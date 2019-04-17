package com.teslyuk.weatherapp.ui

import com.teslyuk.weatherapp.data.model.Weather

interface IMainView {
    fun showCityNameDialog()

    fun showToast(errorCode: Int)

    fun setTitle(title: String)

    fun showHourlyWeather(input: List<Weather>)

    fun showDailyWeather(input: List<Weather>)

    fun showWeatherDescription(description: String?)

    fun showMinTemperature(minTemp: String)

    fun showMaxTemperature(maxTemp: String)

    fun showHumidity(maxTemp: String)

    fun showTemperature(maxTemp: String)

    fun showWeatherIcon(icon: String?)
}