package com.gayathri.basicweatherapp.domain.repo

import com.gayathri.basicweatherapp.data.model.GeoResponse
import com.gayathri.basicweatherapp.data.model.WeatherResponse
import retrofit2.Response

interface WeatherRepository {

    suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Response<WeatherResponse>

    suspend fun getCity(
        city: String
    ): Response<List<GeoResponse>>
}