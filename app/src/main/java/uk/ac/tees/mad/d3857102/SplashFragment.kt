package uk.ac.tees.mad.d3857102

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class SplashFragment: Fragment(R.layout.splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            SplashScreen()
            object : Thread() {
                override fun run() {
                    try {
                        sleep(1000)
                    } catch (e: Exception) {
                    } finally {

                        if(isUserLoggedIn()){
                            val intent = Intent(requireContext(), DashboardActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }else{
                            val intent = Intent(requireContext(), LoginScreenActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }

                    }
                }
            }.start()
        }

    }
    fun isUserLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }
    @Composable
    fun SplashScreen() {

        val scale = remember {
            Animatable(0f)
        }

        LaunchedEffect(true) {
            scale.animateTo(1f, tween(1500))
            delay(3000)
        }

        Column(
            Modifier
                .fillMaxSize()
                .background(color = Color(android.graphics.Color.parseColor("#ff944d")))
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.weight(2f), verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ethnic),
                    contentDescription = "",
                    modifier = Modifier
                        .size(300.dp)
                        .scale(scale.value)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .alpha(scale.value)
                        .scale(scale.value),
                    fontSize = 32.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover, Adorn, Celebrate!",
                    color = Color.White,
                    modifier = Modifier
                        .alpha(scale.value)
                        .scale(scale.value),
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}