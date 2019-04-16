package com.teslyuk.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod") var pod: String
)

data class Wind(
    @SerializedName("speed") var speed: Double,
    @SerializedName("deg") var deg: Double
)

data class Rain(
    @SerializedName("3h") var last3h: Double
)



data class Clouds(
    @SerializedName("all") var all: Int
)