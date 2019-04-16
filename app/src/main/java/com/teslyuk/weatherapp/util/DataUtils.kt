package com.teslyuk.weatherapp.util

import android.text.format.DateFormat
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.*

fun Double.formatKelvinToCelsius(): String {
    return "${(this - 273.15).format(2)}*C"
}

fun Int.formatHumidity(): String {
    return "$this%"
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

fun ImageView.loadWeatherIcon(iconCode: String?) {
    if (iconCode == null) return
    GlideApp.with(context)
        .load("http://openweathermap.org/img/w/$iconCode.png")
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun Long.getFullDate(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("kk-mm dd-MM", cal).toString()
}

fun Long.getDate(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("dd-MM", cal).toString()
}

fun Long.getDayOfWeek(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("EEE", cal).toString()
}

fun Long.getTime(): String {
    val cal = Calendar.getInstance(Locale.ENGLISH)
    cal.timeInMillis = this * 1000
    return DateFormat.format("kk-mm", cal).toString()
}