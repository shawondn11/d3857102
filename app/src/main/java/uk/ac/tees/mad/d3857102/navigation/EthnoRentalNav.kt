package uk.ac.tees.mad.d3857102.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3857102.screens.auth.OnboardingDest
import uk.ac.tees.mad.d3857102.screens.auth.OnboardingScreen
import uk.ac.tees.mad.d3857102.screens.auth.SignInDest
import uk.ac.tees.mad.d3857102.screens.auth.SignInScreen
import uk.ac.tees.mad.d3857102.screens.auth.SignUpDest
import uk.ac.tees.mad.d3857102.screens.auth.SignUpScreen
import uk.ac.tees.mad.d3857102.screens.homescreen.HomeDestination
import uk.ac.tees.mad.d3857102.screens.homescreen.HomeScreen
import uk.ac.tees.mad.d3857102.ui.theme.SplashDestination
import uk.ac.tees.mad.d3857102.ui.theme.SplashScreen

@Composable
fun EthnoRentalNav(
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val firebase = FirebaseAuth.getInstance()
    val user = firebase.currentUser

    val startDestination =
        if (user != null) {
            HomeDestination.route
        } else {
            OnboardingDest.route
        }

    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(
                onComplete = {
                    scope.launch(Dispatchers.Main) {
                        navController.popBackStack()
                        navController.navigate(startDestination)
                    }
                }
            )
        }

        composable(OnboardingDest.route) {
            OnboardingScreen(
                onSignIn = { navController.navigate(SignInDest.route) },
                onSignUp = { navController.navigate(SignUpDest.route) }
            )
        }

        composable(SignInDest.route) {
            SignInScreen(
                onLoginComplete = {
                    context.makeToast("Signed in Successfully")
                    navController.navigate(HomeDestination.route)
                }
            )
        }

        composable(SignUpDest.route) {
            SignUpScreen(
                onSignupComplete = {
                    context.makeToast("Signed up Successfully")
                    navController.navigate(HomeDestination.route)
                }
            )
        }

        composable(HomeDestination.route) {
            HomeScreen(
                onSignOut = {
                    scope.launch {
                        firebase.signOut()
                        navController.navigate(OnboardingDest.route)
                    }
                }
            )
        }
    }
}

fun Context.makeToast(text: String) {
    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
}