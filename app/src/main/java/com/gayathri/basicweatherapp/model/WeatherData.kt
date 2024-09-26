package com.gayathri.basicweatherapp.data.model

data class WeatherData(
    val name: String,
    val temp: Number,
    val icon: String,
    val temp_max: Number,
    val temp_min: Number,
    val description: String
)