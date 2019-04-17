package com.teslyuk.weatherapp.domain

import com.teslyuk.weatherapp.data.model.Weather
import java.util.*

class FilterDailyWeatherUseCase : UseCase<List<Weather>, List<Weather>>() {
    override fun executeUseCase(requestValues: List<Weather>?) {
        var output = mutableListOf<Weather>()
        for (i in 1..4) {
            var dayWeather = filterWeatherByDay(requestValues!!, i)
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
        responseValue = output
        useCaseCallback?.onSuccess(output)
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
}