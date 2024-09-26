package com.gayathri.basicweatherapp.di

import com.gayathri.basicweatherapp.data.network.ApiService
import com.gayathri.basicweatherapp.repository.GeoRepository
import com.gayathri.basicweatherapp.repository.WeatherRepository
import com.gayathri.basicweatherapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getRetrofit(): ApiService {

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

    }

    @Singleton
    @Provides
    fun providesGeoRepository(api: ApiService): GeoRepository {
        return GeoRepository(api)
    }

    @Singleton
    @Provides
    fun providesWeatherRepository(api: ApiService): WeatherRepository {
        return WeatherRepository(api)
    }
}