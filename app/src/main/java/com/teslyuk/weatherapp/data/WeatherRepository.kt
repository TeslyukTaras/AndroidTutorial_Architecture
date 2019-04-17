package com.teslyuk.weatherapp.data

import com.teslyuk.weatherapp.data.local.LocalWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.data.remote.RemoteWeatherDataSource

class WeatherRepository : IWeatherDataSource {

    private val localDS = LocalWeatherDataSource()
    private val remoteDS = RemoteWeatherDataSource()

    override fun getWeather(city: String, callback: IWeatherDataSource.IWeatherCallback) {
        loadFromLocal(city, callback)
    }

    private fun loadFromLocal(city: String, callback: IWeatherDataSource.IWeatherCallback) {
        localDS.getWeather(city, object : IWeatherDataSource.IWeatherCallback {
            override fun onReceived(city: String, data: List<Weather>) {
                callback.onReceived(city, data)
            }

            override fun onFailure(errorCode: Int) {
                loadFromRemote(city, callback)
            }
        })
    }

    private fun loadFromRemote(city: String, callback: IWeatherDataSource.IWeatherCallback) {
        remoteDS.getWeather(city, object : IWeatherDataSource.IWeatherCallback {
            override fun onReceived(city: String, data: List<Weather>) {
                callback.onReceived(city, data)
                localDS.saveToDb(city, data)//save to local db
            }

            override fun onFailure(errorCode: Int) {
                callback.onFailure(errorCode)
            }
        })
    }
}