package com.teslyuk.weatherapp.ui

import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.util.formatHumidity
import com.teslyuk.weatherapp.util.formatKelvinToCelsius
import java.util.*

class MainPresenter(val view: IMainView) {

    private val model by lazy { MainModel(this) }

    fun fetchData() {
        model.loadWeather()
    }

    fun onWeatherReceived(cityName: String, weather: List<Weather>) {
        displayTitle(cityName)
        display24HourWeather(filter24HourWeather(weather))
        display5DayWeather(generateDailyWeather(weather))
        if (weather.isNotEmpty() && filterWeatherNow(weather) != null)
            displayCurrentWeather(filterWeatherNow(weather)!!)
    }

    private fun filterWeatherNow(input: List<Weather>): Weather? {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -2) //2 hours ago
        var currentTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, 5)// 3 hours from now
        var oneDayAfter = calendar.timeInMillis
        return input.firstOrNull { it.time * 1000 in currentTime..oneDayAfter }
    }

    private fun filter24HourWeather(input: List<Weather>): List<Weather> {
        var calendar = Calendar.getInstance()
        var currentTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, 24)
        var oneDayAfter = calendar.timeInMillis
        return input.filter { it.time * 1000 in currentTime..oneDayAfter }
    }

    private fun generateDailyWeather(input: List<Weather>): List<Weather> {
        var output = mutableListOf<Weather>()
        for (i in 1..4) {
            var dayWeather = filterWeatherByDay(input, i)
            var minTemp = Double.MAX_VALUE
            var maxTemp = Double.MIN_VALUE
            var minHumidity = Int.MAX_VALUE
            var maxHumidity = Int.MIN_VALUE
            var icons = mutableSetOf<String>()
            dayWeather.forEach {
                if (it.temp > maxTemp) maxTemp = it.temp
                if (it.temp < minTemp) minTemp = it.temp

                if (it.humidity > maxHumidity) maxHumidity = it.humidity
                if (it.humidity < minHumidity) minHumidity = it.humidity
                if (it.icon != null) {
                    icons.add(it.icon!!)
                }
            }
            var weather = Weather()

            var calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.add(Calendar.DAY_OF_YEAR, i)

            weather.time = calendar.timeInMillis / 1000
            weather.humidity = (minHumidity + maxHumidity) / 2
            weather.icon = icons.firstOrNull()
            weather.tempMin = minTemp
            weather.tempMax = maxTemp
            output.add(weather)
        }
        return output
    }

    private fun filterWeatherByDay(input: List<Weather>, days: Int): List<Weather> {
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.add(Calendar.DAY_OF_YEAR, days)
        var fromTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, 24)
        var oneDayAfter = calendar.timeInMillis
        return input.filter { it.time * 1000 in fromTime..oneDayAfter }
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

    fun onError(errorCode: Int) {
        view.showToast(errorCode)
    }

    fun onChangeCityClick() {
        view.showCityNameDialog()
    }

    fun onCitySelected(cityName: String) {
        model.loadWeatherForCity(cityName)
    }
}