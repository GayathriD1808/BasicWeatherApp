package com.gayathri.basicweatherapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gayathri.basicweatherapp.utils.Constants.DEFAULT_CITY
import com.gayathri.basicweatherapp.network.Result
import com.gayathri.basicweatherapp.utils.DataError
import com.gayathri.basicweatherapp.domain.usecase.GetCityWeatherUseCase
import com.gayathri.basicweatherapp.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetCityWeatherUseCase,
) : ViewModel() {

    // state of weather api
    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState> get() = _homeScreenState

    init {
        fetchWeeklyData(DEFAULT_CITY)
    }

    private fun fetchWeeklyData(city: String) {
        _homeScreenState.value = _homeScreenState.value.copy(isLoading = true, error = null)

        useCase(city)
            .onEach { result ->
                _homeScreenState.value = when (result) {
                    is Result.Error -> {
                        _homeScreenState.value.copy(
                            isLoading = false,
                            error = (result.error as DataError).asUiText(),
                        )
                    }

                    is Result.Success -> {
                        _homeScreenState.value.copy(
                            isLoading = false,
                            weatherInfo = result.data,
                            error = null,
                            needRetryScreen = false,
                            searchedText = city
                        )
                    }
                }

            }.launchIn(viewModelScope)
    }

    fun searchCityAndGetWeather(city: String) {
        fetchWeeklyData(city)
    }

    fun onTextChanged(newText: String) {
        _homeScreenState.value =
            _homeScreenState.value.copy(isValidSearch = true)
    }

    fun onRetry() {
        fetchWeeklyData(DEFAULT_CITY)
    }
}
