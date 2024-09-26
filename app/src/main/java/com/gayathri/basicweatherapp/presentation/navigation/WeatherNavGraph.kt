package com.gayathri.basicweatherapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gayathri.basicweatherapp.presentation.home.HomeScreen

@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController, startDestination = Screens.HomeScreen, modifier = modifier
    ) {

        composable<Screens.HomeScreen> {
            HomeScreen()
        }
    }
}
