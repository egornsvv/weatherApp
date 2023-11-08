package com.example.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.DailyWeatherData

class WeatherDateAdapter(private val allWeatherData: List<DailyWeatherData>) : RecyclerView.Adapter<WeatherDateAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewDate = itemView.findViewById<TextView>(R.id.textViewDate)
        val textViewMinTemp = itemView.findViewById<TextView>(R.id.textViewMin)
        val textViewMaxTemp = itemView.findViewById<TextView>(R.id.textViewMax)
        val textViewCloudiness = itemView.findViewById<TextView>(R.id.textViewСloudy)
        val textViewHumidity = itemView.findViewById<TextView>(R.id.textViewHumidity)
        val textViewPressure = itemView.findViewById<TextView>(R.id.textViewPressure)
        val textViewWindSpeed = itemView.findViewById<TextView>(R.id.textViewWind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyWeatherData = allWeatherData[position]
        holder.textViewDate.text = "Date: ${dailyWeatherData.date}"
        holder.textViewMinTemp.text = "Min Temp: ${dailyWeatherData.minTemperature}"
        holder.textViewMaxTemp.text = "Max Temp: ${dailyWeatherData.maxTemperature}"
        holder.textViewCloudiness.text = "Cloudiness: ${dailyWeatherData.cloudiness}"
        holder.textViewHumidity.text = "Humidity: ${dailyWeatherData.humidity}"
        holder.textViewPressure.text = "Pressure: ${dailyWeatherData.pressure}"
        holder.textViewWindSpeed.text = "Wind Speed: ${dailyWeatherData.windSpeed}"
    }

    override fun getItemCount(): Int {
        return allWeatherData.size
    }
}