package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RestaurantListScreen(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onNavigate: (String) -> Unit,
    restaurants: List<RestaurantEntity> = emptyList(),
    favoriteRestaurants: List<RestaurantEntity> = emptyList()
) {
    var search by remember { mutableStateOf("") }
    BackHandler(onBack = onBack)
    val filtered = restaurants.filter {
        it.name.contains(search.trim(), true) ||
            it.cuisines.split(",").any { tag -> tag.trim().contains(search.trim(), true) }
    }
    Scaffold(bottomBar = {
        Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = onBack) { Text("Home") }
            TextButton(onClick = { onNavigate("Map") }) { Text("Map") }
            TextButton(onClick = onAdd) { Text("Add") }
            TextButton(onClick = { onNavigate("About") }) { Text("About") }
        }
    }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Restaurants", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(search, { search = it }, Modifier.fillMaxWidth(), label = { Text("Search by name or tag") }, singleLine = true)
            if (filtered.isEmpty()) Text(if (restaurants.isEmpty()) "No restaurants saved yet." else "No restaurants found.", color = Color.Gray)
            filtered.forEach { item ->
                Card(Modifier.fillMaxWidth().clickable { onRestaurantClick(item.name) }) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(item.cuisines, color = Color(0xFF60AD65))
                        Text("★ ${item.rating}/5")
                        Text(item.address)
                    }
                }
            }
        }
    }
}
