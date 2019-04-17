package com.teslyuk.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.teslyuk.weatherapp.R
import com.teslyuk.weatherapp.data.model.Weather
import com.teslyuk.weatherapp.util.*

class DailyWeatherAdapter(private var list: List<Weather>) :
    RecyclerView.Adapter<DailyWeatherAdapter.WeatherDayViewHolder>() {

    class WeatherDayViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val icon = root.findViewById<ImageView>(R.id.iconIv)!!
        val time = root.findViewById<TextView>(R.id.timeTv)!!
        val temperatureMin = root.findViewById<TextView>(R.id.temperatureMinTv)!!
        val temperatureMax = root.findViewById<TextView>(R.id.temperatureMaxTv)!!
        val humidity = root.findViewById<TextView>(R.id.humidityTv)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherDayViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.row_daily_weather, parent, false)
        return WeatherDayViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        var weather = list[position]

        holder.time.text = weather.time.getDayOfWeek()
        holder.temperatureMin.text = weather.tempMin.formatKelvinToCelsius()
        holder.temperatureMax.text = weather.tempMax.formatKelvinToCelsius()
        holder.humidity.text = weather.humidity.formatHumidity()
        if (position % 2 == 0) {
            holder.root.setBackgroundColor(ContextCompat.getColor(holder.root.context, R.color.colorPrimary_44))
        } else {
            holder.root.setBackgroundColor(ContextCompat.getColor(holder.root.context, R.color.colorPrimary_66))
        }

        holder.icon.loadWeatherIcon(weather.icon)
    }

    override fun getItemCount() = list.size

    fun updateData(newData: List<Weather>) {
        list = newData
        notifyDataSetChanged()
    }
}

