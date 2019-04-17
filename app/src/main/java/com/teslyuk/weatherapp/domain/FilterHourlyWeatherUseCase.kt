package com.teslyuk.weatherapp.domain

import com.teslyuk.weatherapp.data.model.Weather
import java.util.*

class FilterHourlyWeatherUseCase : UseCase<List<Weather>, List<Weather>>() {
    override fun executeUseCase(requestValues: List<Weather>?) {
        var calendar = Calendar.getInstance()
        var currentTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, 24)
        var oneDayAfter = calendar.timeInMillis

        var output = requestValues!!.filter { it.time * 1000 in currentTime..oneDayAfter }
        responseValue = output
        useCaseCallback?.onSuccess(output)
    }
}