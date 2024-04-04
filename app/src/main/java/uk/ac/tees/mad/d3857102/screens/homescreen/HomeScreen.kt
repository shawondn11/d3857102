package uk.ac.tees.mad.d3857102.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import uk.ac.tees.mad.d3857102.navigation.NavDestination

@Composable
fun HomeScreen(
    onSignOut: () -> Unit
) {
    Column {
        Text(text = "Home")
        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}

object HomeDestination : NavDestination {
    override val route: String
        get() = "home"
}