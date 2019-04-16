package com.teslyuk.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("cod") var cod: String,
    @SerializedName("message") var message: Double,
    @SerializedName("cnt") var cnt: Int,
    @SerializedName("list") var list: List<WeatherCut>,
    @SerializedName("city") var city: CityInfo
)