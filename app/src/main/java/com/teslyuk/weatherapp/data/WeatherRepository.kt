package com.teslyuk.weatherapp.data

import com.teslyuk.weatherapp.data.local.LocalWeatherDataSource
import com.teslyuk.weatherapp.data.remote.RemoteWeatherDataSource
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class WeatherRepository : IWeatherDataSource {

    private val localDS = LocalWeatherDataSource()
    private val remoteDS = RemoteWeatherDataSource()

    override fun getWeather(city: String): Single<IWeatherDataSource.WeatherData> {
        return Single.zip(
            localDS.getWeather(city), remoteDS.getWeather(city),
            BiFunction<IWeatherDataSource.WeatherData, IWeatherDataSource.WeatherData, IWeatherDataSource.WeatherData>
            { local, remote ->
                if (local.data.isNotEmpty()) {
                    local
                } else {
                    localDS.saveToDb(remote)
                    remote
                }
            })
    }
}