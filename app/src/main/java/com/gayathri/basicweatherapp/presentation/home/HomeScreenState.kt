package com.gayathri.basicweatherapp.presentation.home

import com.gayathri.basicweatherapp.data.model.WeatherData
import com.gayathri.basicweatherapp.utils.UiText

data class HomeScreenState(
    val isLoading: Boolean = false,
    val isValidSearch: Boolean = false,
    val searchedText: String = "",
    val weatherInfo: WeatherData? = null,
    val needRetryScreen: Boolean = true,
    val error: UiText? = null
)