package com.example.weather_forecast.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecast.R
import com.example.weather_forecast.model.CityInfo


class MainFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var citiesData : List<CityInfo> = listOf()

    fun setCities(data: List<CityInfo>) {
        citiesData = data
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

        fun bind(cityInfo: CityInfo, position: Int) {
            itemView.apply {
                findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = cityInfo.city
                setOnClickListener { onItemViewClickListener?.onItemViewClick(cityInfo) }
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(cityInfo: CityInfo)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) =
        holder.bind(citiesData[position], position)


    override fun getItemCount() = citiesData.size
}
