package uk.ac.tees.mad.d3857102.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3857102.screens.homescreen.HomeDestination
import uk.ac.tees.mad.d3857102.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3857102.ui.theme.SplashDestination
import uk.ac.tees.mad.d3857102.ui.theme.SplashScreen

@Composable
fun EthnoRentalNav(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(onComplete = {
                scope.launch(Dispatchers.Main) {
                    navController.popBackStack()
                    navController.navigate(HomeDestination.route)
                }
            })
        }
        composable(HomeDestination.route) {
            HomeScreen()
        }
    }
}