package com.gayathri.basicweatherapp.presentation.navigation

@kotlinx.serialization.Serializable
sealed class Screens {

    @kotlinx.serialization.Serializable
    data object HomeScreen : Screens()

}