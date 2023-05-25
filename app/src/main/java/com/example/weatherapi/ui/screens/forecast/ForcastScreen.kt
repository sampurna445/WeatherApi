package com.example.weatherapi.ui.screens.forecast

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.weatherapi.R
import com.example.weatherapi.data.model.Weather
import com.example.weatherapi.ui.navigation.NavBar
import com.example.weatherapi.utils.getCurrentDate
import com.google.gson.Gson
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("LongLogTag")
@Composable
fun ForecastScreen(
    navController: NavController,
    forecastViewModel: ForecastViewModel
) {
    val gson = Gson()
    val currentWeatherObjectsList = forecastViewModel.weatherObjectList.collectAsState().value
    Log.d("CURRENT WEATHER OBJECT LIST", "$currentWeatherObjectsList")
    val currentWeatherObject = currentWeatherObjectsList[0]
    Log.d("WEATHER", currentWeatherObject.weather)
    val weatherData = gson.fromJson(currentWeatherObject.weather, Weather::class.java)

    val gradientColors = listOf(Color(0xFF060620), MaterialTheme.colors.primary)

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
                /*ForecastMainElements()
                HourlyForecastData(
                    data = weatherData
                )
                NextForecast()
                DailyForecastData(data = weatherData)*/
            }
        }, bottomBar = {
            NavBar(navController)
        }, containerColor = Color.Transparent)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastMainElements() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.forecast_report),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                "Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Text(
                getCurrentDate(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}

/*
@Composable
fun HourlyForecastData(data: Weather) {

    LazyRow {
        itemsIndexed(items = data.hourly) { index, item: Hourly ->
            val icon = data.hourly[index].weather[0].icon
            var image = R.drawable.cloudy
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
            val instant = Instant.ofEpochSecond(item.dt.toLong())
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val time = instant.atZone(ZoneId.of("UTC")).format(formatter)
            HourlyCard(
                image = image,
                time = time,
                temperature = item.temp.toInt().toString()
            )

        }
    }
}

@Composable
fun HourlyCard(image: Int, time: String, temperature: String) {
    var color = MaterialTheme.colors.primaryVariant
    var tapped by remember {
        mutableStateOf(false)
    }
    if (tapped) {
        color = MaterialTheme.colors.secondary
    }
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(80.dp)
            .padding(start = 15.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { tapped = true }
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.6f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = image), contentDescription = stringResource(R.string.weather_icon))

            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = time, fontSize = 16.sp, fontFamily = poppinsFamily, color = Color.White)
                Text(
                    text = "$temperature°C",
                    fontSize = 18.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

        }
    }
}

@Composable
fun NextForecast() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                stringResource(R.string.next_forecast),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = poppinsFamily
            )
        }
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "Calendar",
                tint = Color(0xFFd68118)
            )
        }
    }

}

@Composable
fun DailyForecastData(data: Weather) {
    LazyColumn(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
        itemsIndexed(items = data.daily) { index, item: Daily ->
            val icon = data.daily[index].weather[0].icon
            var image = R.drawable.cloudy
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
            val unixTime = item.dt
            val instant = Instant.ofEpochSecond(unixTime.toLong())
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))
            val dayOfWeek = zonedDateTime.dayOfWeek.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val month = zonedDateTime.month.name.lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            val date = zonedDateTime.dayOfMonth
            val monthDate = "$month, $date"
            DailyCard(
                day = dayOfWeek,
                date = monthDate,
                temperature = item.temp.day.toInt().toString(),
                image = image
            )
        }
    }
}

@Composable
fun DailyCard(day: String, date: String, temperature: String, image: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(bottom = 15.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primaryVariant),
        elevation = CardDefaults.cardElevation(500.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day,
                    fontSize = 16.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = date,
                    fontSize = 12.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Light,
                    color = Color.White,
                    textAlign = TextAlign.Start
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$temperature°C",
                    fontSize = 26.sp,
                    fontFamily = poppinsFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            Column(
                modifier = Modifier.padding(end = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = image), contentDescription = stringResource(R.string.weather_icon))

            }
        }
    }

}*/
