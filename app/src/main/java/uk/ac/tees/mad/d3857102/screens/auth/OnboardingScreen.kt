package uk.ac.tees.mad.d3857102.screens.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3857102.navigation.NavDestination
import uk.ac.tees.mad.d3857102.ui.theme.Orange

@Composable
fun OnboardingScreen(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Orange)
            .padding(24.dp)
    ) {
        AppHeader(color = Color.White)

        Spacer(modifier = Modifier.weight(1f))


        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = onSignIn,
                shape = RectangleShape,
                modifier = Modifier
                    .height(70.dp)
                    .width(250.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Sign in", fontSize = 24.sp, color = Orange)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onSignUp,
                shape = RectangleShape,
                modifier = Modifier
                    .height(70.dp)
                    .width(250.dp)
                    .border(BorderStroke(2.dp, Color.White))
            ) {
                Text(text = "Sign up", fontSize = 24.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun AppHeader(
    color: Color
) {
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "ETHNO-RENTAL",
            fontSize = 40.sp,
            fontWeight = FontWeight.Medium,
            color = color,
            letterSpacing = 3.sp,
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

object OnboardingDest : NavDestination {
    override val route: String = "onboarding"

}