package com.gayathri.basicweatherapp.domain.usecase

import com.gayathri.basicweatherapp.network.Result
import com.gayathri.basicweatherapp.network.safeApiCall
import com.gayathri.basicweatherapp.data.model.WeatherData
import com.gayathri.basicweatherapp.domain.mapper.WeatherResponseToModel
import com.gayathri.basicweatherapp.utils.DataError
import com.gayathri.basicweatherapp.utils.Error
import com.gayathri.basicweatherapp.domain.repo.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCityWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val mapper: WeatherResponseToModel
) {
    operator fun invoke(city: String
    ): Flow<Result<WeatherData, Error>> = flow {
        val result = safeApiCall {
            repository.getCity(city)
        }
        when (result) {
            is Result.Success -> {
                if(result.data.isEmpty())
                    emit(Result.Error(DataError.CustomError("No City Found")))
                else {
                    val finalResult = safeApiCall {
                        repository.getWeather(
                            result.data[0].lat,
                            result.data[0].lon
                        )
                    }
                    when (finalResult) {
                        is Result.Success -> emit(Result.Success(mapper.mapFrom(finalResult.data)))
                        is Result.Error -> emit(Result.Error(finalResult.error))
                    }
                }
            }
            is Result.Error -> emit(Result.Error(result.error))
        }
    }
}