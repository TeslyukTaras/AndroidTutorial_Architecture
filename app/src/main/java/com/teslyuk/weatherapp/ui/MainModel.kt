package com.teslyuk.weatherapp.ui

import com.teslyuk.weatherapp.BuildConfig
import com.teslyuk.weatherapp.R
import com.teslyuk.weatherapp.WeatherApp
import com.teslyuk.weatherapp.data.local.AppDatabase
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.data.model.WeatherResponse
import com.teslyuk.weatherapp.data.remote.OpenWeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainModel(var controller: MainController) {

    private var localDatabase: AppDatabase = WeatherApp.instance!!.db
    private var remoteApi: OpenWeatherApi = getWeatherApi()
    private var cityName = TEST_CITY_NAME

    companion object {
        const val TEST_CITY_NAME = "Lviv"
    }

    fun loadWeather() {
        var cashedWeather = localDatabase.weatherDao().getAll()
        if (cashedWeather.isNotEmpty()) {
            controller.onWeatherReceived(cityName, cashedWeather)
        } else {
            loadWeatherForCity(cityName)
        }
    }

    // API calls
    private fun getWeatherApi(): OpenWeatherApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(interceptor)
        }.retryOnConnectionFailure(true).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenWeatherApi::class.java)
    }

    fun loadWeatherForCity(city: String) {
        cityName = city
        remoteApi.weatherByCity(cityName, BuildConfig.WEATHER_API_KEY).enqueue(object :
            Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.body() != null) {
                    var convertedWeather = mutableListOf<Weather>()
                    response.body()!!.list.forEach {
                        convertedWeather.add(Weather(it))
                    }
                    localDatabase.weatherDao().deleteAll()
                    localDatabase.weatherDao().insert(convertedWeather)
                    loadWeather()
                } else {
                    controller.onError(R.string.no_data_available)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
                controller.onError(R.string.request_failure)
            }
        })
    }
}