
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
    var selectedRestaurant by remember { mutableStateOf("Piano Piano") }
    var previousScreen by remember { mutableStateOf("Home") }

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

    BackHandler(
        enabled = screen == "About" ||
                screen == "Add" ||
                screen == "Map"
    ) {
        screen = "Home"
    }

    BackHandler(enabled = screen == "Details") {
        screen = previousScreen
    }

    val navigate: (String) -> Unit = { destination ->
        when (destination) {
            "Home" -> screen = "Home"
            "Saved", "List" -> screen = "List"
            "About" -> screen = "About"
            "Map" -> screen = "Map"
            "Add", "+" -> screen = "Add"

            else -> {
                Toast.makeText(
                    context,
                    "$destination screen will be connected later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val openRestaurant: (String) -> Unit = { name ->
        if (!DeletedRestaurants.isDeleted(name)) {
            selectedRestaurant = name
            previousScreen = screen
            screen = "Details"
        }
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
                    screen = "Add"
                },
                onRestaurantClick = { name ->
                    openRestaurant(name)
                },
                onNavigate = { destination ->
                    navigate(destination)
                }
            )
        }

        "List" -> {
            RestaurantListScreen(
                onBack = {
                    screen = "Home"
                },
                onAdd = {
                    screen = "Add"
                },
                onRestaurantClick = { name ->
                    openRestaurant(name)
                },
                onNavigate = { destination ->
                    navigate(destination)
                }
            )
        }

        "Map" -> {
            MapScreen(
                onBack = {
                    screen = "Home"
                },
                onAddClick = {
                    screen = "Add"
                },
                onNavigate = navigate
            )
        }

        "About" -> {
            AboutScreen(
                onNavigate = navigate
            )
        }

        "Details" -> {
            RestaurantDetailScreen(
                restaurantName = selectedRestaurant,
                onNavigate = navigate,
                onDelete = {
                    screen = "Home"

                    Toast.makeText(
                        context,
                        "$selectedRestaurant deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }

        "Add" -> {
            AddRestaurantScreen(
                onNavigate = navigate,
                onSave = { restaurant ->
                    AddedRestaurants.restaurants.add(restaurant)

                    Toast.makeText(
                        context,
                        "${restaurant.name} saved in this session",
                        Toast.LENGTH_SHORT
                    ).show()

                    screen = "Home"
                }
            )
        }
    }
}
