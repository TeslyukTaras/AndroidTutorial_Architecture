package com.teslyuk.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class MainInfo(
    @SerializedName("temp") var temp: Double,
    @SerializedName("temp_min") var tempMin: Double,
    @SerializedName("temp_max") var tempMax: Double,
    @SerializedName("pressure") var pressure: Double,
    @SerializedName("sea_level") var seaLevel: Double,
    @SerializedName("grnd_level") var grndLevel: Double,
    @SerializedName("humidity") var humidity: Int,
    @SerializedName("temp_kf") var tempKf: Double
)