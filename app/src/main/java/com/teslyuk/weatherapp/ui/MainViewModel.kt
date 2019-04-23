package com.teslyuk.weatherapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teslyuk.weatherapp.data.WeatherRepository
import com.teslyuk.weatherapp.data.local.LocalWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.domain.UseCase
import com.teslyuk.weatherapp.domain.FilterDailyWeatherUseCase
import com.teslyuk.weatherapp.domain.FilterHourlyWeatherUseCase
import com.teslyuk.weatherapp.domain.FilterNowWeatherUseCase
import com.teslyuk.weatherapp.domain.GetWeatherUseCase

class MainViewModel : ViewModel() {

    private val getWeather by lazy { GetWeatherUseCase(WeatherRepository()) }
    private val filterDailyWeather by lazy { FilterDailyWeatherUseCase() }
    private val filterHourlyWeather by lazy { FilterHourlyWeatherUseCase() }
    private val filterNowWeather by lazy { FilterNowWeatherUseCase() }

    val currentWeather: MutableLiveData<Weather> by lazy {
        MutableLiveData<Weather>()
    }

    val hourlyWeather: MutableLiveData<List<Weather>> by lazy {
        MutableLiveData<List<Weather>>()
    }

    val dailyWeather: MutableLiveData<List<Weather>> by lazy {
        MutableLiveData<List<Weather>>()
    }

    val cityName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun fetchData() {
        getWeather(LocalWeatherDataSource.TEST_CITY_NAME)
    }

    fun getWeather(city: String) {
        cityName.value = city
        getWeather.run(city, object : UseCase.UseCaseCallback<List<Weather>> {
            override fun onSuccess(response: List<Weather>) {
                onWeatherReceived(response)
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun onWeatherReceived(weather: List<Weather>) {
        if (weather.isNotEmpty()) {
            filterHourlyWeather.run(weather, object : UseCase.UseCaseCallback<List<Weather>> {
                override fun onSuccess(response: List<Weather>) {
                    hourlyWeather.value = response
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })

            filterDailyWeather.run(weather, object : UseCase.UseCaseCallback<List<Weather>> {
                override fun onSuccess(response: List<Weather>) {
                    dailyWeather.value = response
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
            filterNowWeather.run(weather, object : UseCase.UseCaseCallback<Weather?> {
                override fun onSuccess(response: Weather?) {
                    if (response != null) currentWeather.value = response
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
}