package com.example.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.databinding.ItemPropertyBinding

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var weatherDataList = mutableListOf<DailyWeatherData>()
    private var onItemClickListener: OnWeatherItemClickListener? = null

    fun setOnItemClickListener(listener: OnWeatherItemClickListener) {
        onItemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = weatherDataList[position]
        holder.bind(weatherData)
        holder.itemView.setOnClickListener {
            onItemClickListener?.onWeatherItemClick(weatherData)
        }
    }
    override fun getItemCount(): Int {
        return weatherDataList.size
    }
    fun submitList(newList: List<DailyWeatherData>) {
        weatherDataList.clear()
        weatherDataList.addAll(newList)
        notifyDataSetChanged()
    }
    inner class WeatherViewHolder(itemView: ItemPropertyBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val dateTextView: TextView = itemView.textViewDate
        private val cloudinessTextView: TextView = itemView.textViewLoudy
        private val humidityTextView: TextView = itemView.textViewHumidity
        private val pressureTextView: TextView = itemView.textViewPressure
        private val minTempTextView: TextView = itemView.textViewMin
        private val maxTempTextView: TextView = itemView.textViewMax
        private val windSpeedTextView: TextView = itemView.textViewWind

        fun bind(weatherData: DailyWeatherData) {
            dateTextView.text = "Date: ${weatherData.date}"
            cloudinessTextView.text = "Cloudiness: ${weatherData.cloudiness}%"
            humidityTextView.text = "Humidity: ${weatherData.humidity}%"
            pressureTextView.text = "Pressure: ${weatherData.pressure} hPa"
            minTempTextView.text = "Min Temp: ${weatherData.minTemperature}°C"
            maxTempTextView.text = "Max Temp: ${weatherData.maxTemperature}°C"
            windSpeedTextView.text = "Wind Speed: ${weatherData.windSpeed} m/s"
        }
        init {
            itemView.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                }
            }
        }
    }
    interface OnWeatherItemClickListener {
        fun onWeatherItemClick(weatherData: DailyWeatherData)
    }

}