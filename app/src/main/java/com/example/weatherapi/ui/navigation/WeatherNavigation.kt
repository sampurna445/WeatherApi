package com.example.weatherapi.ui.navigation

import android.content.Context
import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherapi.ui.screens.forecast.ForecastScreen
import com.example.weatherapi.ui.screens.forecast.ForecastViewModel
import com.example.weatherapi.ui.screens.main.MainViewModel
import com.example.weatherapi.ui.screens.main.WeatherScreen
import com.example.weatherapi.ui.screens.search.FavouriteViewModel
import com.example.weatherapi.ui.screens.search.SearchScreen
import com.example.weatherapi.ui.screens.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherNavigation(context: Context) {
    val navController = rememberNavController()
    val mainViewModel = hiltViewModel<MainViewModel>()
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
    val forecastViewModel = hiltViewModel<ForecastViewModel>()

    NavHost(navController = navController, startDestination = com.example.weatherapi.ui.navigation.Screen.Splash.route) {
        composable(com.example.weatherapi.ui.navigation.Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        val route = BottomNavItem.Home.route
        composable("$route/{city}", arguments = listOf(navArgument(name = "city") {
            type = NavType.StringType
        })) { navBack ->
            navBack.arguments?.getString("city").let { city ->
                WeatherScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                    forecastViewModel = forecastViewModel,
                    context = context,
                    city = city
                )
            }
        }
        composable(BottomNavItem.Forecast.route) {
            ForecastScreen(navController = navController, forecastViewModel)
        }
        composable(BottomNavItem.Search.route) {
            SearchScreen(navController = navController, context, favouriteViewModel, mainViewModel)
        }
        /*composable(BottomNavItem.Settings.route) {
            SettingScreen(navController = navController)
        }*/
    }
}