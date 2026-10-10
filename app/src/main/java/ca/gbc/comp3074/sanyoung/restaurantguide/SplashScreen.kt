package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

@Composable
fun SplashScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF4F5E32)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.restaurant_logo),
                contentDescription = "Restaurant Guide logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Personal\nRestaurant Guide",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                lineHeight = 42.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Discover & remember\nyour favourite places",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1.5f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    RestaurantGuideTheme {
        SplashScreen()
    }
}