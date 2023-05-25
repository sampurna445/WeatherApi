package com.example.weatherapi.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapi.data.database.WeatherDao
import com.example.weatherapi.data.database.WeatherDatabase
import com.example.weatherapi.data.remote.WeatherApiDetails
import com.example.weatherapi.data.remote.WeatherApiRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// To make sure that this is indeed a dagger module
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    // Set up all modules needed across the entire application such as databases, repositories
    /* Create a provider to provide the Once Call API */
    @Provides
    @Singleton
    fun provideOneCallApi(): WeatherApiRequest {
        return Retrofit.Builder()
            .baseUrl(WeatherApiDetails.BASE_URL)
            // For retrofit to convert all the JSON gotten here into actual objects
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiRequest::class.java)

        /* Once this is built and run, it will know to create all the classes that it needs
        to create this dependency
         */
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao =
        weatherDatabase.weatherDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        ).fallbackToDestructiveMigration().build()
}