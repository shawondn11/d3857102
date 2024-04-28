package uk.ac.tees.mad.d3857102

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.d3857102.ui.theme.EnthenoRental2Theme
import android.view.View
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


class LoginScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnthenoRental2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LoginScreen()
                }
            }
        }
    }

    @Composable
    fun LoginScreen() {
        val context = LocalContext.current
        val scale = remember { Animatable(0f) }
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }

        LaunchedEffect(true) {
            scale.animateTo(1f, tween(1500))
        }



        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color(android.graphics.Color.parseColor("#ffffff")))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ethnic),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .scale(scale.value)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "ENTHENO-RENTAL",
                    fontSize = 25.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(android.graphics.Color.parseColor("#ff944d")),
                    modifier = Modifier
                        .alpha(scale.value)
                        .scale(scale.value)
                )

                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "LOGIN",
                    fontSize = 21.sp,
                    letterSpacing = 1.0.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(android.graphics.Color.parseColor("#ff944d")),
                    modifier = Modifier
                        .alpha(scale.value)
                        .scale(scale.value)
                )


                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color(android.graphics.Color.parseColor("#3F51B5"))),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(android.graphics.Color.parseColor("#ff944d")),
                        unfocusedBorderColor = Color(android.graphics.Color.parseColor("#ff944d")),
                        cursorColor = Color(android.graphics.Color.parseColor("#ff944d"))
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(color = Color(android.graphics.Color.parseColor("#3F51B5"))),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(android.graphics.Color.parseColor("#ff944d")),
                        unfocusedBorderColor = Color(android.graphics.Color.parseColor("#ff944d")),
                        cursorColor = Color(android.graphics.Color.parseColor("#ff944d"))
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (emailState.value.isNotEmpty() && passwordState.value.isNotEmpty()) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailState.value, passwordState.value)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val intent = Intent(this@LoginScreenActivity, DashboardActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Login failed, get the error message
                                        val errorMessage = task.exception?.message
                                        if (errorMessage != null) {
                                            Toast.makeText(this@LoginScreenActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    Toast.makeText(this@LoginScreenActivity, exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                        } else {
                            Toast.makeText(this@LoginScreenActivity, "Username or password is incorrect", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(android.graphics.Color.parseColor("#ff944d")))
                ) {
                    Text(
                        text = "Login In",
                        color = Color(android.graphics.Color.parseColor("#ffffff")),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Row with Sign Up text
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(this@LoginScreenActivity, SIgnUpActivityScreen::class.java)
                            startActivity(intent)
                            finish()
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account?",
                        color = Color(android.graphics.Color.parseColor("#ff944d")),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    Text(
                        text = "Sign Up",
                        color = Color(android.graphics.Color.parseColor("#ff944d")),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 2.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                }

            }
        }
    }

}
