package uk.ac.tees.mad.d3857102.screens.auth

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3857102.R
import uk.ac.tees.mad.d3857102.data.repository.GoogleAuthClient
import uk.ac.tees.mad.d3857102.navigation.NavDestination
import uk.ac.tees.mad.d3857102.ui.theme.Black
import uk.ac.tees.mad.d3857102.ui.theme.Orange
import uk.ac.tees.mad.d3857102.ui.theme.grey

@Composable
fun SignInScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginComplete: () -> Unit
) {
    val signInStatus = viewModel.signInState.collectAsState(initial = null)
    val signInState = viewModel.state.collectAsState().value
    val loginUiState = viewModel.loginUiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInWithGoogleResult(signInResult)
                }
            }
        }
    )


    LaunchedEffect(key1 = signInStatus.value?.isSuccess) {
        scope.launch {
            if (signInStatus.value?.isSuccess?.isNotEmpty() == true) {
                focusManager.clearFocus()
                val success = signInStatus.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                onLoginComplete()
            }
        }
    }

    LaunchedEffect(key1 = signInStatus.value?.isError) {
        scope.launch {
            if (signInStatus.value?.isError?.isNotEmpty() == true) {
                val error = signInStatus.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if (signInState.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()
            val user = googleAuthUiClient.getSignedInUser()
            if (user != null) {
                viewModel.saveUserInFirestore(user)
            }
            viewModel.resetState()
            onLoginComplete()
        }
    }
    LaunchedEffect(key1 = signInState.signInError) {
        scope.launch {
            if (signInState.signInError?.isNotEmpty() == true) {
                val error = signInState.signInError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppHeader(color = Black)
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            Modifier.padding(horizontal = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = loginUiState.email,
                onValueChange = {
                    viewModel.updateLoginState(loginUiState.copy(email = it))
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                ),
                placeholder = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "Email", textAlign = TextAlign.Center, fontSize = 22.sp)
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = grey,
                    focusedIndicatorColor = Black
                ),
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextField(
                value = loginUiState.password,
                onValueChange = {
                    viewModel.updateLoginState(loginUiState.copy(password = it))
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                ),
                singleLine = true,
                placeholder = {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "Password", textAlign = TextAlign.Center, fontSize = 22.sp)
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = grey,
                    focusedIndicatorColor = Black
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
            )
            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = { viewModel.loginUser(loginUiState.email, loginUiState.password) },
                shape = RectangleShape,
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) {
                Row(
                    Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (signInStatus.value?.isLoading == true) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                    } else {
                        Text(text = "Sign in", fontSize = 24.sp, color = Color.White)
                    }
                }

            }
            Spacer(modifier = Modifier.height(60.dp))
            Text(text = "or", color = grey, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .width(250.dp)
                    .border(BorderStroke(2.dp, Black))
                    .clickable {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest
                                    .Builder(
                                        signInIntentSender ?: return@launch
                                    )
                                    .build()
                            )
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(25.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = "Sign in with Google", fontSize = 18.sp, color = Color.Gray)
            }
        }
    }
}

object SignInDest: NavDestination{
    override val route: String = "signin"
}