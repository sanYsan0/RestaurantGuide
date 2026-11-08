package ca.gbc.comp3074.sanyoung.restaurantguide

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RestaurantGuideTheme { RestaurantGuideApp() } }
    }
}

@Composable
fun RestaurantGuideApp() {
    var screen by remember { mutableStateOf("Splash") }
    var selectedRestaurant by remember { mutableStateOf("") }
    var editingRestaurant by remember { mutableStateOf<RestaurantEntity?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dao = remember { RestaurantDatabase.getDatabase(context).restaurantDao() }
    val factory = remember(dao) { RestaurantViewModelFactory(dao) }
    val vm: RestaurantViewModel = viewModel(factory = factory)
    val databaseRestaurants by vm.restaurants.collectAsState()

    val restaurantForms = databaseRestaurants.map { item ->
        NewRestaurant(
            name = item.name,
            cuisines = item.cuisines.split(",").map { it.trim() }.filter { it.isNotBlank() },
            rating = item.rating,
            address = item.address,
            phone = item.phone,
            notes = item.notes,
            photo = item.photo
        )
    }

    LaunchedEffect(Unit) {
        delay(1800)
        screen = "Home"
    }

    BackHandler(enabled = screen == "Splash") {}
    BackHandler(enabled = screen == "Details" || screen == "Add" || screen == "About" || screen == "Map") {
        screen = "Home"
        editingRestaurant = null
    }

    val navigate: (String) -> Unit = { destination ->
        screen = when (destination) {
            "Saved", "List", "Map" -> "List"
            "Add", "+" -> "Add"
            "About" -> "About"
            else -> "Home"
        }
    }

    when (screen) {
        "Splash" -> SplashScreen()
        "Home" -> HomeScreen(
            onSeeAll = { screen = "List" },
            onAdd = { editingRestaurant = null; screen = "Add" },
            onRestaurantClick = { selectedRestaurant = it; screen = "Details" },
            onNavigate = navigate,
            addedRestaurants = restaurantForms
        )
        "List" -> RestaurantListScreen(
            onBack = { screen = "Home" },
            onAdd = { editingRestaurant = null; screen = "Add" },
            onRestaurantClick = { selectedRestaurant = it; screen = "Details" },
            onNavigate = navigate,
            restaurants = databaseRestaurants
        )
        "About" -> AboutScreen(onNavigate = navigate)
        "Details" -> {
            val selected = databaseRestaurants.firstOrNull { it.name == selectedRestaurant }
            RestaurantDetailScreen(
                restaurantName = selectedRestaurant,
                databaseRestaurant = selected,
                onNavigate = navigate,
                onEdit = { editingRestaurant = selected; screen = "Add" },
                onSaveRating = { vm.updateRating(selectedRestaurant, it) },
                onDelete = {
                    vm.deleteRestaurant(selectedRestaurant)
                    screen = "Home"
                    Toast.makeText(context, "Restaurant deleted", Toast.LENGTH_SHORT).show()
                }
            )
        }
        "Add" -> AddRestaurantScreen(
            onNavigate = navigate,
            initialRestaurant = editingRestaurant,
            onSave = { item ->
                scope.launch {
                    try {
                        vm.addRestaurant(item)
                        Toast.makeText(context, "Restaurant saved", Toast.LENGTH_SHORT).show()
                        editingRestaurant = null
                        screen = "Home"
                    } catch (error: Exception) {
                        Toast.makeText(context, "Could not save restaurant: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}
