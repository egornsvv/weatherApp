package com.example.weatherapp.data.repository

import com.example.weatherapp.data.api.WeatherApi
import com.example.weatherapp.data.model.WeatherResponse

class WeatherRepository(private val weatherApi: WeatherApi) {
    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String, units: String): WeatherResponse {
        return weatherApi.getWeatherForecast(lat, lon, apiKey, units)
    }
}