package com.teslyuk.weatherapp.domain

import com.teslyuk.weatherapp.data.IWeatherDataSource
import com.teslyuk.weatherapp.data.WeatherRepository
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.domain.UseCase

class GetWeatherUseCase(private val repository: WeatherRepository) :
    UseCase<String, List<Weather>>() {
    override fun executeUseCase(requestValues: String?) {
        repository.getWeather(requestValues!!, object : IWeatherDataSource.IWeatherCallback {
            override fun onReceived(city: String, data: List<Weather>) {
                responseValue = data
                useCaseCallback?.onSuccess(data)
            }

            override fun onFailure(errorCode: Int) {
                useCaseCallback?.onError(Throwable("Data not found"))
            }
        })
    }
}