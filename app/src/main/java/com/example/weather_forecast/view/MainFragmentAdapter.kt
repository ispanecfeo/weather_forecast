package com.example.weather_forecast.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecast.R
import com.example.weather_forecast.model.WeatherInfo

class MainFragmentAdapter(private var onItemViewClickListener:OnItemViewClickListener?) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var weatherData : List<WeatherInfo> = listOf()

    fun setWeather(data: List<WeatherInfo>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_main_recycler_item, parent, false) as View
        )
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weatherInfo: WeatherInfo) {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weatherInfo.cityInfo.city
            itemView.setOnClickListener {
               onItemViewClickListener?.onItemViewClick(weatherInfo)
            }
        }

    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weatherInfo: WeatherInfo)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

}
