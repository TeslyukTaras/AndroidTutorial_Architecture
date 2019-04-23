package com.teslyuk.weatherapp.data.remote

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.teslyuk.weatherapp.BuildConfig
import com.teslyuk.weatherapp.data.IWeatherDataSource
import com.teslyuk.weatherapp.data.model.Weather
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(OpenWeatherApi::class.java)
    }

    override fun getWeather(city: String): Single<IWeatherDataSource.WeatherData> {
        return remoteApi.weatherByCity(city, BuildConfig.WEATHER_API_KEY)
            .map {
            var convertedWeather = mutableListOf<Weather>()
            it!!.list.forEach { weather ->
                convertedWeather.add(Weather(weather))
            }
            IWeatherDataSource.WeatherData(city, convertedWeather)
        }
    }
}