package com.example.weather_forecast.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forecast.R
import com.example.weather_forecast.model.HistoryCity


class HistoryFragmentAdapter(private val listener: (HistoryCity) -> Unit ) : RecyclerView.Adapter<HistoryFragmentAdapter.HistoryViewHolder>() {

    private var data: List<HistoryCity> = listOf()

    fun setData(data: List<HistoryCity>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(historyCity: HistoryCity, clickListener : (HistoryCity) -> Unit) {
            itemView.apply {
                findViewById<TextView>(R.id.historyRecyclerCity).text = historyCity.city
                findViewById<TextView>(R.id.historyRecyclerData).text = String.format(
                            context.getString(R.string.city_coordinates),
                            historyCity.lat.toString(),
                            historyCity.lon.toString())

                itemView.setOnClickListener { clickListener(historyCity) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_history_recyclerview_item, parent, false) as View
        )
    }


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount() = data.size

}