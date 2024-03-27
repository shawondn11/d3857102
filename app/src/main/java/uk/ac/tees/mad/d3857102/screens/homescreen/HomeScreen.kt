package uk.ac.tees.mad.d3857102.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import uk.ac.tees.mad.d3857102.navigation.NavigationDestination

@Composable
fun HomeScreen() {
    Column {
        Text(text = "Home")
    }
}

object HomeDestination: NavigationDestination{
    override val route: String
        get() = "home"
}