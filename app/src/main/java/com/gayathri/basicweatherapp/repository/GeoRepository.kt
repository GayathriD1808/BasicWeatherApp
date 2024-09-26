package com.gayathri.basicweatherapp.repository

import com.gayathri.basicweatherapp.data.model.GeoResponse
import com.gayathri.basicweatherapp.data.network.ApiService
import javax.inject.Inject

class GeoRepository @Inject constructor(
    private val geoApiService: ApiService
) {
    suspend fun searchCity(query: String): List<GeoResponse> {
        return geoApiService.searchCity(query)
    }
}
