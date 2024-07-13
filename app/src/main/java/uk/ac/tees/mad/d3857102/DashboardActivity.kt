package uk.ac.tees.mad.d3857102

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.d3857102.ui.theme.EnthenoRental2Theme
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.remember

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnthenoRental2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }
    }

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Account,
            NavigationItem.Rent
        )
//    val profilePicture: Painter = painterResource(id = R.drawable.profile_picture)
//    val name = "Hi, " +"John Doe"

        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = MaterialTheme.colors.background) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route


                    items.forEach {
                        BottomNavigationItem(selected = currentRoute == it.route,
                            label = {
                                Text(
                                    text = it.label,
                                    color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = it.icons, contentDescription = null,
                                    tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )

                            },

                            onClick = {
                                if (currentRoute != it.route) {

                                    navController.graph?.startDestinationRoute?.let {
                                        navController.popBackStack(it, true)
                                    }

                                    navController.navigate(it.route) {
                                        launchSingleTop = true
                                    }

                                }

                            })

                    }

                }


            },
        ) {

            NavigationController(navController = navController)

        }

    }


    @Composable
    fun NavigationController(navController: NavHostController) {
        NavHost(navController = navController, startDestination = NavigationItem.Home.route) {

            composable(NavigationItem.Home.route) {
                Home(this@DashboardActivity)

            }

            composable(NavigationItem.Account.route) {
                NewProfileScreen(this@DashboardActivity)
            }

            composable(NavigationItem.Rent.route) {
                Rent(this@DashboardActivity)
            }

        }
    }


    @Composable
    fun Rent(context: Context) {
        var wearable by remember { mutableStateOf<List<Wearables>>(emptyList()) }
        var uid = FirebaseAuth.getInstance().getCurrentUser()?.uid
        LaunchedEffect(uid) {
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("users").document(uid).collection("rent")
                    .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val wear = mutableListOf<Wearables>()
                        val result = task.result
                        result?.let { querySnapshot ->

                            for (document in querySnapshot) {
                                val name = document.getString("name") ?: ""
                                val imageUrl = document.getString("image_url") ?: ""
                                val description = document.getString("description") ?: ""
                                val price = document.getLong("price") ?: 0
                                val lat = document.getDouble("lat") ?: 0.0
                                val long = document.getDouble("long") ?: 0.0

                                val wearableModel =
                                    Wearables(
                                        name,
                                        price.toInt(),
                                        imageUrl,
                                        description,
                                        lat.toDouble(),
                                        long.toDouble()
                                    )
                                wear.add(wearableModel)
                            }

                        }
                        wearable = wear

                    } else {
                        println("manan there is some error")
                    }
                }
            }
        }

        if (wearable.isEmpty()) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp)
                    .fillMaxSize(), // Ensures the Column takes up the full available size
                verticalArrangement = Arrangement.Center, // Centers the content vertically
                horizontalAlignment = Alignment.CenterHorizontally // Centers the content horizontally
            ) {
                Text(
                    text = "No Dresses have been Rented",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

        } else {

            LazyColumn(

            ) {
                items(wearable) { rowItems ->
                    WearableCard(rowItems, context, true)
                }

                // Add some spacing between the LazyColumn and the TotalAmountButton
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }


    }

    @Composable
    fun Home(context: Context) {
        var wearable by remember { mutableStateOf<List<Wearables>>(emptyList()) }
        var uid = true
        LaunchedEffect(uid) {
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("wearables").get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val wear = mutableListOf<Wearables>()
                            val result = task.result
                            result?.let { querySnapshot ->

                                for (document in querySnapshot) {
                                    val name = document.getString("name") ?: ""
                                    val imageUrl = document.getString("image_url") ?: ""
                                    val description = document.getString("description") ?: ""
                                    val price = document.getLong("price") ?: 0
                                    val lat = document.getDouble("lat") ?: 0.0
                                    val long = document.getDouble("long") ?: 0.0

                                    println("Name: $name")
                                    println("Image URL: $imageUrl")
                                    println("Description: $description")
                                    println("Price: $price")
                                    println("Latitude: $lat")
                                    println("Longitude: $long")


                                    val wearableModel =
                                        Wearables(
                                            name,
                                            price.toInt(),
                                            imageUrl,
                                            description,
                                            lat.toDouble(),
                                            long.toDouble()
                                        )
                                    wear.add(wearableModel)
                                }

                            }
                            wearable = wear

                        } else {
                            println("manan there is some error")
                        }
                    }
            }
        }

        LazyColumn(

        ) {
            items(wearable) { rowItems ->
                WearableCard(rowItems, context, false)
            }

            // Add some spacing between the LazyColumn and the TotalAmountButton
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

    }

    @Composable
    fun WearableCard(wearable: Wearables, context: Context, is_rented: Boolean) {
        val localContext = LocalContext.current
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .padding(4.dp)
                .aspectRatio(0.8f)
                .clickable {
                    val intent = Intent(context, WearableActivity::class.java)
                    intent.putExtra("image_url", wearable.image_url)
                    intent.putExtra("title", wearable.name)
                    intent.putExtra("description", wearable.description)
                    intent.putExtra("lat", wearable.latitude)
                    intent.putExtra("long", wearable.longitude)
                    intent.putExtra("price", wearable.price)
                    intent.putExtra("isRented", is_rented)
                    localContext.startActivity(intent)
                }
        ) {
            println("manan" + wearable.description)
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(wearable.image_url),
                    contentDescription = "${wearable.name} image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = wearable.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$ ${wearable.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(Modifier.weight(1f))

                }
            }
        }
    }


}
