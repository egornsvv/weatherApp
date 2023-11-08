package com.example.weatherapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.DailyWeatherData
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.repository.WeatherRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.min
import kotlin.math.max

class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    val weatherResponse = MutableLiveData<WeatherResponse>()
    private val _weatherData = MutableLiveData<List<DailyWeatherData>>()
    val weatherData: LiveData<List<DailyWeatherData>> = _weatherData
    private val _weatherDataByDate = MutableLiveData<MutableMap<String, MutableList<DailyWeatherData>>>()
    val weatherDataByDate: LiveData<MutableMap<String, MutableList<DailyWeatherData>>> = _weatherDataByDate

    fun processWeatherData(response: WeatherResponse) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val weatherDataMap = mutableMapOf<String, DailyWeatherData>()

        for (weatherData in response.list) {
            val date = dateFormat.parse(weatherData.dt_txt)
            val dateString = dateFormat.format(date)

            val existingData = weatherDataMap[dateString]
            if (existingData != null) {
                existingData.minTemperature = min(existingData.minTemperature, weatherData.main.temp_min)
                existingData.maxTemperature = max(existingData.maxTemperature, weatherData.main.temp_max)
            } else {
                val dailyWeatherData = DailyWeatherData(
                    date = dateString,
                    minTemperature = weatherData.main.temp_min,
                    maxTemperature = weatherData.main.temp_max,
                    cloudiness = weatherData.clouds.all,
                    humidity = weatherData.main.humidity,
                    pressure = weatherData.main.pressure,
                    windSpeed = weatherData.wind.speed
                )
                weatherDataMap[dateString] = dailyWeatherData
            }
        }

        val consolidatedWeatherList = weatherDataMap.values.toList()
        _weatherData.value = consolidatedWeatherList
    }
    fun processWeatherByDate(currentDate: String, response: WeatherResponse) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val weatherDataByDate = mutableMapOf<String, MutableList<DailyWeatherData>>()

        for (weatherData in response.list) {
            val date = dateFormat.parse(weatherData.dt_txt)
            if (currentDate == date?.let { dateFormat.format(it) }) {
                val dailyWeatherData = DailyWeatherData(
                    date = weatherData.dt_txt,
                    minTemperature = weatherData.main.temp_min,
                    maxTemperature = weatherData.main.temp_max,
                    cloudiness = weatherData.clouds.all,
                    humidity = weatherData.main.humidity,
                    pressure = weatherData.main.pressure,
                    windSpeed = weatherData.wind.speed
                )
                if (weatherDataByDate.containsKey(currentDate)) {
                    weatherDataByDate[currentDate]?.add(dailyWeatherData)
                } else {
                    weatherDataByDate[currentDate] = mutableListOf(dailyWeatherData)
                }
            }
        }
        _weatherDataByDate.value = weatherDataByDate
    }



    suspend fun getWeather(lat: Double, lon: Double, apiKey: String, units: String): WeatherResponse {
        return weatherRepository.getWeatherForecast(lat, lon, apiKey, units)
    }
}