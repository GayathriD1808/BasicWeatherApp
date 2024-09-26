package com.gayathri.basicweatherapp.repository

import com.gayathri.basicweatherapp.data.model.WeatherResponse
import com.gayathri.basicweatherapp.data.network.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse {
        return apiService.getWeatherByCity(lat, lon)
    }
}
