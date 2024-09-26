package com.gayathri.basicweatherapp.presentation.home

import com.gayathri.basicweatherapp.data.model.Main
import com.gayathri.basicweatherapp.data.model.Weather

data class HomeScreenState(
    val weatherInfo: Weather? = null,
    val cityName: String? = null,
    val main: Main? = null
)