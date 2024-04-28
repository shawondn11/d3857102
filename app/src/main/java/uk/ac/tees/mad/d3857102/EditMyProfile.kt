package uk.ac.tees.mad.d3857102

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import uk.ac.tees.mad.d3857102.ui.theme.EnthenoRental2Theme

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class EditMyProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnthenoRental2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EditProfile()
                }
            }
        }
    }


    @Composable
    fun EditProfile() {
        val localContext = LocalContext.current
        var user by remember { mutableStateOf(
            user(
                name = "Zoody",
                email = "zoody.sam@gmail.com",
                phone = "+21 123-456-8888",
                address = "Main Street, City",
                image_url = "https://firebasestorage.googleapis.com/v0/b/ethnorental.appspot.com/o/DALL%C2%B7E-2024-04-15-13.17.jpg?alt=media&token=8f7dcd67-1766-4f16-ab42-5b03e8b7f862",
                user_id = "abc"
            )
        ) }

        var loading by remember { mutableStateOf(true) }

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Effect to fetch data from Firestore when the composable is first created
        LaunchedEffect(uid) {
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val userData = document.toObject(user::class.java)
                            if (userData != null) {
                                // Update the user data
                                user = user(
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
                        loading = false
                    }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (loading) {
                // Display a loading indicator while data is being fetched
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                // Data has been fetched, display the EditProfileScreen1
                EditProfileScreen1(user, onBack = {
                    val intent = Intent(this@EditMyProfile, DashboardActivity::class.java)
                    localContext.startActivity(intent)
                },)
            }
        }
    }

    @Composable
    fun EditProfileScreen1(
        user: user,
        onBack: () -> Unit,
    ) {
        var editedUser by remember { mutableStateOf(user.copy()) }
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Edit Profile",
                            fontWeight = FontWeight.Bold,
                            color = Color(android.graphics.Color.parseColor("#ff944d"))
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(android.graphics.Color.parseColor("#ff944d"))
                            )
                        }
                    },
                    backgroundColor = Color.White,
                    elevation = 0.dp
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberImagePainter(editedUser.image_url),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .clickable { openCamera() },
                        contentScale = ContentScale.Crop
                    )
                }

                // Editable fields
                EditableProfileInfoItem("Name", editedUser.name) { editedUser = editedUser.copy(name = it) }
                EditableProfileInfoItem("Email", editedUser.email) { editedUser = editedUser.copy(email = it) }
                EditableProfileInfoItem("Phone", editedUser.phone) { editedUser = editedUser.copy(phone = it) }
                EditableProfileInfoItem("Address", editedUser.address) { editedUser = editedUser.copy(address = it) }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                        val userData = user(editedUser.name,editedUser.phone,editedUser.image_url,editedUser.user_id,editedUser.address,editedUser.email)
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("users").document(uid).set(userData).addOnCompleteListener {
                                val intent = Intent(this@EditMyProfile, DashboardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.addOnFailureListener {
                                    exception ->
                                Toast.makeText(this@EditMyProfile, exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(backgroundColor =Color(android.graphics.Color.parseColor("#ff944d")))
                ) {
                    Text("Save Profile", color = Color.White)
                }
            }
        }
    }

    @Composable
    fun EditableProfileInfoItem(
        label: String,
        value: String,
        onValueChange: (String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle1,
                color = Color(android.graphics.Color.parseColor("#ff944d"))
            )

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    backgroundColor = Color.Transparent,
                    cursorColor = Color(android.graphics.Color.parseColor("#ff944d")),
                    focusedIndicatorColor = Color(android.graphics.Color.parseColor("#ff944d")),
                    unfocusedIndicatorColor = Color(android.graphics.Color.parseColor("#ff944d"))
                )
            )
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    private fun openCamera() {
        if (checkPermissionsCamera()) {
            if (isCameraPermissionEnabled()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }}
        }
        else{
            Log.e("man log" , "requesting for camera permission")
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun isCameraPermissionEnabled(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 123

    private fun checkPermissionsCamera(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                openCamera()
            } else {
                // Permission denied, handle accordingly (show a message, etc.)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val storage = FirebaseStorage.getInstance()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.image.setImageBitmap(imageBitmap)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child("profile_photo").child(fileName)
            // Convert the Bitmap to a byte array
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        Log.d("MainActivity", "Download URL: $downloadUrl")
                        val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid()
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val userData = document.toObject(user::class.java)
                                        if (userData != null) {
                                            // Update the user data
                                            val user = user(
                                                userData.name,
                                                userData.phone,
                                                downloadUrl,
                                                userData.user_id,
                                                userData.address,
                                                userData.email
                                            )
                                            FirebaseFirestore.getInstance().collection("users").document(uid).set(user)
                                            Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    } else {
                                        // The document does not exist
                                    }
                                }

                        }

                    } } else {
                    val exception = task.exception
                }
            }
        }
    }




}

