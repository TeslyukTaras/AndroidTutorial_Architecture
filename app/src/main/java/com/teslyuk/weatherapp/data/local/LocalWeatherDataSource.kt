package com.teslyuk.weatherapp.data.local

import com.teslyuk.weatherapp.WeatherApp
import com.teslyuk.weatherapp.data.IWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather

class LocalWeatherDataSource : IWeatherDataSource {

    private var localDatabase: AppDatabase = WeatherApp.instance!!.db
    private var cityName = TEST_CITY_NAME


    companion object {
        const val TEST_CITY_NAME = "Lviv"
    }

    override fun getWeather(city: String, callback: IWeatherDataSource.IWeatherCallback) {
        if (city != cityName)
            callback.onFailure(-1)

        var cashedWeather = localDatabase.weatherDao().getAll()
        if (cashedWeather.isNotEmpty()) {
            callback.onReceived(cityName, cashedWeather)
        } else {
            callback.onFailure(0)
        }
    }

    fun saveToDb(city: String, items: List<Weather>) {
        cityName = city
        localDatabase.weatherDao().deleteAll()
        localDatabase.weatherDao().insert(items)
    }
}