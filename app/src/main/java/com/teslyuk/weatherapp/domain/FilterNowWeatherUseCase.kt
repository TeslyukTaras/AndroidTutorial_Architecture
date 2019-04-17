package com.teslyuk.weatherapp.domain

import com.teslyuk.weatherapp.data.model.Weather
import java.util.*

class FilterNowWeatherUseCase : UseCase<List<Weather>, Weather?>() {
    override fun executeUseCase(requestValues: List<Weather>?) {
        var calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, -2) //2 hours ago
        var currentTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, 5)// 3 hours from now
        var oneDayAfter = calendar.timeInMillis

        var output = requestValues!!.firstOrNull { it.time * 1000 in currentTime..oneDayAfter }
        responseValue = output
        useCaseCallback?.onSuccess(output)
    }
}