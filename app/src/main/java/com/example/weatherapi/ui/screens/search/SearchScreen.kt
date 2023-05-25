package com.example.weatherapi.ui.screens.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapi.R
import com.example.weatherapi.data.database.Favourite
import com.example.weatherapi.data.errorHandling.DataOrException
import com.example.weatherapi.data.model.Weather
import com.example.weatherapi.ui.navigation.BottomNavItem
import com.example.weatherapi.ui.navigation.NavBar
import com.example.weatherapi.ui.screens.main.MainViewModel
import com.example.weatherapi.ui.screens.main.getLatLon
import com.example.weatherapi.utils.InputField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    context: Context,
    favouriteViewModel: FavouriteViewModel,
    mainViewModel: MainViewModel
) {
    val gradientColors = listOf(Color(0xFF060620), MaterialTheme.colors.primary)
    var list = favouriteViewModel.favList.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
    ) {
        Scaffold(content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.pick_location),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(R.string.find_city),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
                SearchBar { city ->
                    val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected: Boolean = activeNetwork?.isConnected == true
                    if (isConnected) {
                        // Navigate to HomeScreen
                        navController.navigate(BottomNavItem.Home.route + "/$city")
                        val address = getLatLon(context, city)
                        val latitude = address?.latitude
                        val longitude = address?.longitude
                        if(latitude != null || longitude != null) {
                            // Insert record to database
                            favouriteViewModel.insertFavourite(
                                Favourite(
                                    city = city,
                                    lat = latitude!!,
                                    lon = longitude!!,
                                )
                            )
                        }
                    } else {
                        Toast.makeText(context, "No internet connection!", Toast.LENGTH_LONG).show()
                    }

                }
                Log.d("TAG", "$list")
                list = list.reversed()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (list.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_favourite),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(list.size) { index ->
                                FavCard(
                                    index = index,
                                    favourite = list[index],
                                    context = context,
                                    navController = navController,
                                    mainViewModel = mainViewModel,
                                    favouriteViewModel = favouriteViewModel
                                )
                            }
                        }
                    }
                }

            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit = {}) {
    val searchState = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(searchState.value) {
        searchState.value.trim().isNotEmpty()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 15.dp, end = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InputField(
            valueState = searchState,
            labelId = "City name",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchState.value.trim())
                keyboardController?.hide()
                searchState.value = ""
            })
    }
}

@Composable
fun FavCard(
    index: Int,
    favourite: Favourite,
    context: Context,
    navController: NavController,
    mainViewModel: MainViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    var color = MaterialTheme.colors.primaryVariant
    if (index == 0) {
        color = MaterialTheme.colors.secondary
    }
    Card(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .clickable {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnected == true
                if (isConnected) {
                    navController.popBackStack()
                    navController.navigate(BottomNavItem.Home.route + "/${favourite.city}")
                } else {
                    Toast
                        .makeText(context, "No internet connection!", Toast.LENGTH_LONG)
                        .show()
                }
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(500.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.getWeatherData(favourite.lat, favourite.lon)
        }.value
        Log.d("Weather", "${weatherData.data}")
        if (weatherData.data != null) {
            Log.d("Weather", "${weatherData.data}")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Text(
                        weatherData.data!!.main?.temp?.toInt().toString() + "°",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                    weatherData.data!!.weather?.get(0)?.description?.split(' ')?.let {
                        Text(
                            it
                                .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraLight
                        )
                    }
                }
                val icon = weatherData.data!!.weather?.get(0)?.icon
                var image = R.drawable.sun_cloudy
                if (icon == "01d") {
                    image = R.drawable.sunny
                } else if (icon == "02d") {
                    image = R.drawable.sunny
                } else if (icon == "03d" || icon == "04d" || icon == "04n" || icon == "03n" || icon == "02n") {
                    image = R.drawable.cloudy
                } else if (icon == "09d" || icon == "10n" || icon == "09n") {
                    image = R.drawable.rainy
                } else if (icon == "10d") {
                    image = R.drawable.rainy_sunny
                } else if (icon == "11d" || icon == "11n") {
                    image = R.drawable.thunder_lightning
                } else if (icon == "13d" || icon == "13n") {
                    image = R.drawable.snow
                } else if (icon == "50d" || icon == "50n") {
                    image = R.drawable.fog
                } else if (icon == "01n") {
                    image = R.drawable.clear
                } else {
                    R.drawable.cloudy
                }
                Image(
                    painter = painterResource(id = image),
                    contentDescription = stringResource(R.string.weather_icon),
                    modifier = Modifier
                        .scale(
                            1F
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.7f)) {
                    Text(favourite.city, fontWeight = FontWeight.Medium)
                }
                Column(verticalArrangement = Arrangement.Bottom) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.deleted_favourite),
                        tint = Color(0xFFd68118),
                        modifier = Modifier.clickable {
                            favouriteViewModel.deleteFavourite(favourite)
                        })
                }
            }
        }
    }
}
