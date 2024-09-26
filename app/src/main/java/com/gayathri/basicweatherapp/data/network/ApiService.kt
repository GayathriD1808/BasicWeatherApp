package com.gayathri.basicweatherapp.data.network

import com.gayathri.basicweatherapp.data.model.GeoResponse
import com.gayathri.basicweatherapp.data.model.WeatherResponse
import com.gayathri.basicweatherapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): WeatherResponse

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = Constants.API_KEY
    ): List<GeoResponse>
}
