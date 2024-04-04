package uk.ac.tees.mad.d3857102.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3857102.R
import uk.ac.tees.mad.d3857102.navigation.NavDestination


object SplashDestination : NavDestination {
    override val route: String
        get() = "splash"
}

@Composable
fun SplashScreen(
    onComplete: () -> Unit
) {

    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(true) {
        scale.animateTo(1f, tween(1500))
        delay(3000)
        onComplete()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Orange)
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