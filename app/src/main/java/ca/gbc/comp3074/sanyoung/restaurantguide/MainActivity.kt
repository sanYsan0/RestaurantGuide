package ca.gbc.comp3074.sanyoung.restaurantguide

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RestaurantGuideTheme {
                RestaurantGuideApp()
            }
        }
    }
}

@Composable
fun RestaurantGuideApp() {
    var screen by remember { mutableStateOf("Splash") }

    val context = LocalContext.current

    // Show Splash for 2 seconds, then open Home.
    LaunchedEffect(Unit) {
        delay(2000)
        screen = "Home"
    }

    // Prevent Back from closing the app during Splash.
    BackHandler(enabled = screen == "Splash") {
        // Wait until Home appears.
    }

    when (screen) {
        "Splash" -> {
            SplashScreen()
        }

        "Home" -> {
            HomeScreen(
                onSeeAll = {
                    screen = "List"
                },
                onAdd = {
                    Toast.makeText(
                        context,
                        "Add screen will be connected later.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onRestaurantClick = { name ->
                    Toast.makeText(
                        context,
                        "Open details: $name",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onNavigate = { destination ->
                    if (destination != "Home") {
                        Toast.makeText(
                            context,
                            "$destination screen will be connected later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }

        "List" -> {
            RestaurantListScreen(
                onBack = {
                    screen = "Home"
                },
                onAdd = {
                    Toast.makeText(
                        context,
                        "Add screen will be connected later.",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onRestaurantClick = { name ->
                    Toast.makeText(
                        context,
                        "Open details: $name",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onNavigate = { destination ->
                    Toast.makeText(
                        context,
                        "$destination screen will be connected later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}