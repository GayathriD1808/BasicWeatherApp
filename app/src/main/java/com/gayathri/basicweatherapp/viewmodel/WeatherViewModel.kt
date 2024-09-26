package com.gayathri.basicweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gayathri.basicweatherapp.data.model.GeoResponse
import com.gayathri.basicweatherapp.data.model.WeatherResponse
import com.gayathri.basicweatherapp.presentation.home.HomeScreenState
import com.gayathri.basicweatherapp.repository.GeoRepository
import com.gayathri.basicweatherapp.repository.WeatherRepository
import com.gayathri.basicweatherapp.utils.Constants.DEFAULT_CITY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val geoRepository: GeoRepository
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState> get() = _homeScreenState

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    init {
        searchCities(DEFAULT_CITY)
    }

    private fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = weatherRepository.getWeather(lat, lon)
                _weatherData.value = response
                _homeScreenState.value = HomeScreenState(weatherInfo = response.weather[0], cityName = response.name, main = response.main)
            } catch (e: Exception) {
                _weatherData.value = null
            }
        }
    }

    fun searchCities(query: String) {
        viewModelScope.launch {
            try {
                val response = geoRepository.searchCity(query)
                selectCity(response[0])
            } catch (e: Exception) {
                _weatherData.value = null
            }
        }
    }

    fun selectCity(response: GeoResponse) {
        loadWeather(response.lat, response.lon)
    }
}
