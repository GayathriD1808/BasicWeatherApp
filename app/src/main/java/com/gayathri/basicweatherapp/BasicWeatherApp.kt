package com.gayathri.basicweatherapp


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gayathri.basicweatherapp.presentation.navigation.WeatherNavHost

@Composable
fun WeatherApp(navController: NavHostController = rememberNavController()) {
    WeatherNavHost(navController = navController)
}