package com.teslyuk.weatherapp.ui

import com.teslyuk.weatherapp.data.WeatherRepository
import com.teslyuk.weatherapp.data.local.LocalWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.domain.UseCase
import com.teslyuk.weatherapp.domain.FilterDailyWeatherUseCase
import com.teslyuk.weatherapp.domain.FilterHourlyWeatherUseCase
import com.teslyuk.weatherapp.domain.FilterNowWeatherUseCase
import com.teslyuk.weatherapp.domain.GetWeatherUseCase
import com.teslyuk.weatherapp.util.formatHumidity
import com.teslyuk.weatherapp.util.formatKelvinToCelsius

class MainPresenter(private val view: IMainView) {

    private val getWeather by lazy { GetWeatherUseCase(WeatherRepository()) }
    private val filterDailyWeather by lazy { FilterDailyWeatherUseCase() }
    private val filterHourlyWeather by lazy { FilterHourlyWeatherUseCase() }
    private val filterNowWeather by lazy { FilterNowWeatherUseCase() }

    fun fetchData() {
        getWeather(LocalWeatherDataSource.TEST_CITY_NAME)
    }

    private fun getWeather(cityName: String) {
        getWeather.run(cityName, object : UseCase.UseCaseCallback<List<Weather>> {
            override fun onSuccess(response: List<Weather>) {
                onWeatherReceived(cityName, response)
            }

            override fun onError(t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun onWeatherReceived(cityName: String, weather: List<Weather>) {
        displayTitle(cityName)
        if (weather.isNotEmpty()) {
            filterHourlyWeather.run(weather, object : UseCase.UseCaseCallback<List<Weather>> {
                override fun onSuccess(response: List<Weather>) {
                    display24HourWeather(response)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })

            filterDailyWeather.run(weather, object : UseCase.UseCaseCallback<List<Weather>> {
                override fun onSuccess(response: List<Weather>) {
                    display5DayWeather(response)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
            filterNowWeather.run(weather, object : UseCase.UseCaseCallback<Weather?> {
                override fun onSuccess(response: Weather?) {
                    if (response != null) displayCurrentWeather(response)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    private fun displayCurrentWeather(weather: Weather) {
        view.showWeatherDescription(weather.description)
        view.showMinTemperature(weather.tempMin.formatKelvinToCelsius())
        view.showMaxTemperature(weather.tempMax.formatKelvinToCelsius())
        view.showHumidity(weather.humidity.formatHumidity())
        view.showTemperature(weather.temp.formatKelvinToCelsius())

        view.showWeatherIcon(weather.icon)
    }

    private fun display24HourWeather(input: List<Weather>) {
        view.showHourlyWeather(input)
    }

    private fun display5DayWeather(input: List<Weather>) {
        view.showDailyWeather(input)
    }

    private fun displayTitle(title: String) {
        view.setTitle(title)
    }

    fun onChangeCityClick() {
        view.showCityNameDialog()
    }

    fun onCitySelected(cityName: String) {
        getWeather(cityName)
    }
}