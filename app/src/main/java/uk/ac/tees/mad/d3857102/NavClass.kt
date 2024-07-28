package uk.ac.tees.mad.d3857102

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem(val route: String, val label: String, val icons: ImageVector) {

    object Home : NavigationItem("home", "Home", Icons.Default.Home)

    object Rent: NavigationItem("Rent","Rent",Icons.Default.AddShoppingCart)


    object Account: NavigationItem("account","Account", Icons.Default.AccountCircle)


}