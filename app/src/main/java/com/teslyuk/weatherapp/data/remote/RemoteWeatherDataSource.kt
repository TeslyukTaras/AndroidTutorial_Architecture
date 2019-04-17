package com.teslyuk.weatherapp.data.remote

import com.teslyuk.weatherapp.BuildConfig
import com.teslyuk.weatherapp.R
import com.teslyuk.weatherapp.data.IWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.data.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteWeatherDataSource : IWeatherDataSource {

    private var remoteApi: OpenWeatherApi = getWeatherApi()

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

    override fun getWeather(city: String, callback: IWeatherDataSource.IWeatherCallback) {
        remoteApi.weatherByCity(city, BuildConfig.WEATHER_API_KEY).enqueue(object :
            Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.body() != null) {
                    var convertedWeather = mutableListOf<Weather>()
                    response.body()!!.list.forEach {
                        convertedWeather.add(Weather(it))
                    }
                    callback.onReceived(city, convertedWeather)
                } else {
                    callback.onFailure(R.string.no_data_available)
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
                callback.onFailure(R.string.request_failure)
            }
        })
    }
}