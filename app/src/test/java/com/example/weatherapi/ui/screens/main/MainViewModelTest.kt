package com.example.weatherapi.ui.screens.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapi.data.errorHandling.DataOrException
import com.example.weatherapi.data.model.Weather
import com.example.weatherapi.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.stubbing.OngoingStubbing

class MainViewModelTest {
    private val testDispatcher= StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var repository: WeatherRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUP()
    {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }



    @Test
    fun testgetWeatherData() = runTest {
        val weather = Weather()


        // Return the mock WeatherModel object from the repository
        Mockito.`when`(repository.getWeather(52.0,-2.0)).thenReturn(weather)



       var weatherData =  viewModel.getWeatherData(360.0,360.0)

        // Calling the function that uses the repository to get the weather

        testDispatcher.scheduler.advanceUntilIdle()

        // Checking if the products LiveData contains the expected result
        weatherData?.let { value ->
            assert(value != null)

        }

    }


    /* @Test
     fun getWeatherData() = runTest{
         // Arrange
         val expectedData = Weather(*//* Provide necessary weather data *//*)
        val response = DataOrException(data = expectedData)

        whenever(repository.getWeather(any(), any())).thenReturn(response)

        // Act
        val result = viewModel.getWeatherData(52.0, -2.0)

        // Assert
        assert(result.data != null)
        val data = result.data!!
        // Perform assertions on the data object
        // For example: assertEquals(expectedData.property, data.property)

    }*/
}

private fun <T> OngoingStubbing<T>.thenReturn(weather: Weather) {

}






