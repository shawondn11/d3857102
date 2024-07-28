package uk.ac.tees.mad.d3857102

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.d3857102.ui.theme.EnthenoRental2Theme
import android.view.View
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.rememberImagePainter
import kotlinx.coroutines.delay

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun NewProfileScreen(context: Context) {
    var currUser by remember { mutableStateOf(user(
        // Assume userModel has appropriate fields
        name = "John Doe",
        email = "john.doe@gamil.com",
        phone = "+1123-456-7890",
        address = "123 Main Street, City",
        image_url = "your_image_url_or_path_here",
        user_id = "abc"
    ))}

    // Replace 'user_id' with the actual field name from your userModel class
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userData = document.toObject(user::class.java)
                        if (userData != null) {
                            // Update the user data
                            currUser = user(
                                userData.name,
                                userData.phone,
                                userData.image_url,
                                userData.user_id,
                                userData.address,
                                userData.email
                            )
                        }
                    } else {
                        // The document does not exist
                    }
                }
        }
    }
    // Main profile screen
    Scaffold(
        backgroundColor = Color.White, // Set background to white
        topBar = {
            TopAppBar(
                title = { Text("My Profile", color = Color.White) },
                backgroundColor = Color(android.graphics.Color.parseColor("#ff944d"))
            )
        }
    ) { paddingValues ->
        ProfileContent(paddingValues, currUser, context)
    }
}

@Composable
fun ProfileContent(paddingValues: PaddingValues, user: user, context: Context) {
    val localContext = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        ProfileImageSection(user)
        ProfileDetailsSection(user)
        ProfileActionButtons(localContext, context)
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ProfileImageSection(user: user) {
    val profileImage = rememberImagePainter(user.image_url)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = profileImage,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ProfileDetailsSection(user: user) {
    // Displaying user info
    ProfileInfoItem("Name", user.name)
    ProfileInfoItem("Email", user.email)
    ProfileInfoItem("Phone", user.phone)
    ProfileInfoItem("Address", user.address)
}

@Composable
fun ProfileActionButtons(localContext: Context, context: Context) {
    // Edit Profile Button
    Button(
        onClick = {
            val intent = Intent(context, EditMyProfile::class.java)
            localContext.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#ff944d")))
    ) {
        Text("Edit Profile", color = Color.White)
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Logout Button
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LoginScreenActivity::class.java)
            localContext.startActivity(intent)
            (context as? Activity)?.finish()
        },
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
    ) {
        Text("Logout", color = Color.White)
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            color = Color(android.graphics.Color.parseColor("#757575")),
            modifier = Modifier.weight(1f) // Assign weight to label for it to occupy left side
        )

        Spacer(Modifier.weight(0.1f)) // This will act as a separator

        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = Color(android.graphics.Color.parseColor("#212121")),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f) // Assign weight to value for it to occupy right side
        )
    }
}
