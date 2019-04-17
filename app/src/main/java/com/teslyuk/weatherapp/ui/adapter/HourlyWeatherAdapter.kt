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

class HourlyWeatherAdapter(private var list: List<Weather>) :
    RecyclerView.Adapter<HourlyWeatherAdapter.WeatherHourViewHolder>() {

    class WeatherHourViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val icon = root.findViewById<ImageView>(R.id.iconIv)!!
        val time = root.findViewById<TextView>(R.id.timeTv)!!
        val temperature = root.findViewById<TextView>(R.id.temperatureTv)!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherHourViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.row_hourly_weather, parent, false)
        return WeatherHourViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WeatherHourViewHolder, position: Int) {
        var weather = list[position]

        holder.time.text = weather.time.getTime()
        holder.temperature.text = weather.temp.formatKelvinToCelsius()
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