package com.gayathri.basicweatherapp.utils

object Constants {
    //endpoints
    const val API_KEY = "102719dafc20e5e7587c2f61b8c20f68"
    const val BASE_URL = "https://api.openweathermap.org/"
    const val ICON_URL = "https://openweathermap.org/img/wn/%s@2x.png"

    //static location
    val DEFAULT_CITY = "New York"
    val DEFAULT_LAT_LONG= Pair(43.7001, -79.4163)

    //SharedPreference Keys
    const val MY_SHARED_PREFERENCES = "mySharedPreferences"
    const val SAVED_CITY_KEY = "saveCityKey"

}