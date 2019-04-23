package com.teslyuk.weatherapp.data.remote

import com.teslyuk.weatherapp.data.model.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("data/2.5/forecast")
    fun weatherByCity(@Query("q") city: String, @Query("apikey") apiKey: String): Single<WeatherResponse>
}