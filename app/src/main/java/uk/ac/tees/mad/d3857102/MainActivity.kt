package uk.ac.tees.mad.d3857102

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.d3857102.navigation.EthnoRentalNav
import uk.ac.tees.mad.d3857102.screens.auth.SignInScreen
import uk.ac.tees.mad.d3857102.screens.auth.SignUpScreen
import uk.ac.tees.mad.d3857102.ui.theme.EthnoRentalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EthnoRentalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    EthnoRentalNav(navController)
                }
            }
        }
    }
}