package com.teslyuk.weatherapp.data.model

import com.google.gson.annotations.SerializedName
import com.teslyuk.weatherapp.data.*

data class WeatherCut(
    @SerializedName("dt") var dt: Long,
    @SerializedName("main") var main: MainInfo,
    @SerializedName("weather") var shortInto: List<ShortInfo>?,
//    @SerializedName("clouds") var clouds: Clouds? = null,
//    @SerializedName("wind") var wind: Wind? = null,
//    @SerializedName("rain") var rain: Rain? = null,
//    @SerializedName("sys") var sys: Sys? = null,
    @SerializedName("dt_txt") var dtTxt: String
)