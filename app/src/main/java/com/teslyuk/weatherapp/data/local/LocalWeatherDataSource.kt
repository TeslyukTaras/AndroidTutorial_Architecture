package com.teslyuk.weatherapp.data.local

import com.teslyuk.weatherapp.WeatherApp
import com.teslyuk.weatherapp.data.IWeatherDataSource
import io.reactivex.Single

class LocalWeatherDataSource : IWeatherDataSource {

    private var localDatabase: AppDatabase = WeatherApp.instance!!.db
    private var cityName = TEST_CITY_NAME

    companion object {
        const val TEST_CITY_NAME = "Lviv"
    }

    override fun getWeather(city: String): Single<IWeatherDataSource.WeatherData> {
        if (city != cityName)
            return Single.create { emiter ->
                emiter.onSuccess(IWeatherDataSource.WeatherData(city, emptyList()))
            }

        return localDatabase.weatherDao().getAll().map {
            IWeatherDataSource.WeatherData(city, it)
        }
    }

    fun saveToDb(data: IWeatherDataSource.WeatherData) {
        cityName = data.city
        localDatabase.weatherDao().deleteAll()
        localDatabase.weatherDao().insert(data.data)
    }
}