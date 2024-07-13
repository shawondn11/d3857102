package uk.ac.tees.mad.d3857102

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.d3857102.ui.theme.EnthenoRental2Theme


import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import coil.compose.rememberImagePainter

import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WearableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image_url = intent.getStringExtra("image_url")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val lat = intent.getDoubleExtra("lat",0.0)
        val long = intent.getDoubleExtra("long",0.0)
        val price = intent.getIntExtra("price",0)
        val is_rented = intent.getBooleanExtra("isRented",false)


        if (title != null) {
            if (image_url != null) {
                if (description != null) {
                    val currWearable = Wearables(title,price,image_url,description,lat,long)
                    setContent {
                        EnthenoRental2Theme {
                            // A surface container using the 'background' color from the theme
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colors.background
                            ) {
                                WearableDetailPage(title, price,description,image_url,lat,long,is_rented)

                            }
                        }
                    }
                }
            }
        }

    }

    @Composable
    fun WearableDetailPage(title:String,price:Int,description:String,imageUrl:String,lat:Double,long:Double,isRented:Boolean){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Image of ${title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Price: $${price}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(android.graphics.Color.parseColor("#ff944d")), // Example color for price
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                if(isRented == false){
                    Button(onClick = {
                        Toast.makeText(this@WearableActivity,"Your item has been successfully Rented",Toast.LENGTH_LONG).show()
                        var uid = FirebaseAuth.getInstance().getCurrentUser()?.uid

                        if (uid != null) {
                            val newWearable = Wearables(title,price,imageUrl,description,lat,long)
                            FirebaseFirestore.getInstance().collection("users").document(uid).collection("rent").add(newWearable)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
    
                                        val intent = Intent(this@WearableActivity, DashboardActivity::class.java)
                                        startActivity(intent)
                                        // Handle the successful addition of the new wearable
                                        Log.d("Firestore", "Document successfully added!")
                                    } else {
                                        // Handle the failure
                                        println("manan more Success ")
                                        task.exception?.let {
                                            Log.e("Firestore", "Error adding document", it)
                                        }
                                    }
                                }
                        }


                    },colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#ff944d")))
                    ) {
                        Text(text = "Rent",color = Color.White)
                    }
                }


                Button(onClick = {
                    val intent = Intent(this@WearableActivity, LocateClientAcitvity::class.java)
                    intent.putExtra("long", long)
                    intent.putExtra("lat", lat)
                    startActivity(intent)
                },colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#ff944d")))) {
                    Text(text = "Locate",color = Color.White)
                }
            }
        }
    }


}
