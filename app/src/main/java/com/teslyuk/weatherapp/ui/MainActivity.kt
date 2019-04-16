package com.teslyuk.weatherapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teslyuk.weatherapp.R
import com.teslyuk.weatherapp.WeatherApp
import com.teslyuk.weatherapp.data.local.AppDatabase
import com.teslyuk.weatherapp.data.remote.OpenWeatherApi
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.util.formatHumidity
import com.teslyuk.weatherapp.util.formatKelvinToCelsius
import com.teslyuk.weatherapp.util.loadWeatherIcon
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.top_container.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.teslyuk.weatherapp.BuildConfig
import com.teslyuk.weatherapp.data.model.WeatherResponse

class MainActivity : AppCompatActivity() {

    companion object {
        const val KEY_TAG = "TEST_LOG"
        const val TEST_CITY_NAME = "Lviv"
    }

    //day
    private lateinit var dayRecyclerView: RecyclerView
    private lateinit var dayViewAdapter: DailyWeatherAdapter
    private lateinit var dayViewManager: RecyclerView.LayoutManager

    //hour
    private lateinit var hourRecyclerView: RecyclerView
    private lateinit var hourViewAdapter: HourlyWeatherAdapter
    private lateinit var hourViewManager: RecyclerView.LayoutManager

    private lateinit var localDatabase: AppDatabase
    private lateinit var remoteApi: OpenWeatherApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()

        localDatabase = (application as WeatherApp).db
        remoteApi = getWeatherApi()
    }

    private fun initView() {
        hourViewManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        hourViewAdapter = HourlyWeatherAdapter(listOf())

        hourRecyclerView = findViewById<RecyclerView>(R.id.hourlyRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = hourViewManager
            adapter = hourViewAdapter
        }

        dayViewManager = LinearLayoutManager(this)
        dayViewAdapter = DailyWeatherAdapter(listOf())

        dayRecyclerView = findViewById<RecyclerView>(R.id.dailyRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = dayViewManager
            adapter = dayViewAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = TEST_CITY_NAME
        loadLocalWeather()
    }

    private fun loadLocalWeather() {
        var cashedWeather = localDatabase.weatherDao().getAll()
        hourViewAdapter.updateData(filter24HourWeather(cashedWeather))
        dayViewAdapter.updateData(generateDailyWeather(cashedWeather))
        if (cashedWeather.isNotEmpty() && filterWeatherNow(cashedWeather) != null)
            displayCurrentWeather(filterWeatherNow(cashedWeather)!!)
        else {
            loadWeatherForCity(TEST_CITY_NAME)
        }
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
        weatherDescriptionTv.text = weather.description
        minTempTv.text = weather.tempMin.formatKelvinToCelsius()
        maxTempTv.text = weather.tempMax.formatKelvinToCelsius()
        humidityTv.text = weather.humidity.formatHumidity()
        temperatureTv.text = weather.temp.formatKelvinToCelsius()

        weatherIconIv.loadWeatherIcon(weather.icon)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_city -> {
                askForCityName()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun askForCityName() {
        val alert = AlertDialog.Builder(this)
        val editText = EditText(this)
        alert.setMessage("Select City")
        alert.setTitle("Enter Your city name")
        alert.setView(editText)
        alert.setPositiveButton("Search") { dialog, whichButton ->
            loadWeatherForCity(editText.text.toString())
        }

        alert.show()
    }

    private fun loadWeatherForCity(cityName: String) {
        toolbar.title = cityName

        remoteApi.weatherByCity(cityName, BuildConfig.WEATHER_API_KEY).enqueue(object :
            Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                Log.d(KEY_TAG, "onResponse:")
                if (response.code() < 300 && response.body() != null) {
                    Log.d(KEY_TAG, "city: ${response.body()!!.city}")
                    var convertedWeather = mutableListOf<Weather>()
                    response.body()!!.list.forEach {
                        convertedWeather.add(Weather(it))
                    }
                    localDatabase.weatherDao().deleteAll()
                    localDatabase.weatherDao().insert(convertedWeather)
                    loadLocalWeather()
                } else {
                    Toast.makeText(this@MainActivity, R.string.no_data_available, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d(KEY_TAG, "onFailure:")
                t.printStackTrace()
                Toast.makeText(this@MainActivity, R.string.request_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

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
}