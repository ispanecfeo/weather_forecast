package com.example.weather_forecast.view.history.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecast.R
import com.example.weather_forecast.model.room.WeatherEntity
import java.text.SimpleDateFormat

class HistoryDetailFragmentAdapter()
    : RecyclerView.Adapter<HistoryDetailFragmentAdapter.HistoryWeatherViewHolder>() {

    private var data: List<WeatherEntity> = listOf()

    fun setData(data: List<WeatherEntity>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class HistoryWeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(weather: WeatherEntity) {
            itemView.apply {
                findViewById<TextView>(R.id.history_fragment_date).text = SimpleDateFormat(context.getString(R.string.pattern_date)).format(weather.date)
                findViewById<TextView>(R.id.history_fragment_temperature).text = weather.temperature.toString() + " " + context.getString(R.string.degree_of_celsius)
                findViewById<TextView>(R.id.history_fragment_options).text = weather.condition
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryWeatherViewHolder {
        return HistoryWeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_detail_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: HistoryWeatherViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int = this.data.size ?: 0


}