package com.gayathri.basicweatherapp.domain.repo

import com.gayathri.basicweatherapp.network.WeatherApi
import com.gayathri.basicweatherapp.data.model.GeoResponse
import com.gayathri.basicweatherapp.data.model.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): Response<WeatherResponse> {
        return weatherApi.getWeather(latitude, longitude)
    }

    override suspend fun getCity(city: String): Response<List<GeoResponse>> {
        return weatherApi.searchCity(city)
    }

}