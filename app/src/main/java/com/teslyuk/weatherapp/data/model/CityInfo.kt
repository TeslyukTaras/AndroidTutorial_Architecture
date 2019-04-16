package com.teslyuk.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class CityInfo(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("coord") var coord: Coord,
    @SerializedName("country") var country: String,
    @SerializedName("population") var population: Int
)