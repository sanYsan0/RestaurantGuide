package ca.gbc.comp3074.sanyoung.restaurantguide

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
fun HomeScreen(
    onSeeAll: () -> Unit,
    onAdd: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onNavigate: (String) -> Unit,
    addedRestaurants: List<NewRestaurant> = emptyList()
) {
    var search by remember { mutableStateOf("") }
    val green = Color(0xFF60AD65)
    val results = addedRestaurants.filter {
        it.name.contains(search.trim(), true) ||
            it.cuisines.any { tag -> tag.contains(search.trim(), true) }
    }
    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onNavigate("Home") }) { Text("Home") }
                TextButton(onClick = onAdd) { Text("Add") }
                TextButton(onClick = onSeeAll) { Text("Restaurants") }
                TextButton(onClick = { onNavigate("About") }) { Text("About") }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Personal Restaurant Guide", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(search, { search = it }, Modifier.fillMaxWidth(), label = { Text("Search by restaurant name or tag") }, singleLine = true)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("My Restaurants", fontSize = 21.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onSeeAll) { Text("View All") }
            }
            Button(onClick = onAdd, Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = green)) { Text("Add Restaurant") }
            if (results.isEmpty()) {
                Text(if (addedRestaurants.isEmpty()) "No restaurants saved yet." else "No restaurants found.", color = Color.Gray)
            }
            results.forEach { item ->
                Card(Modifier.fillMaxWidth().clickable { onRestaurantClick(item.name) }) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(item.cuisines.joinToString(" · "), color = green)
                        Text("★ ${item.rating}/5")
                        Text(item.address)
                    }
                }
            }
        }
    }
}
