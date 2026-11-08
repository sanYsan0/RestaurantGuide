package ca.gbc.comp3074.sanyoung.restaurantguide

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RestaurantDetailScreen(
    restaurantName: String = "",
    onNavigate: (String) -> Unit,
    onDelete: () -> Unit,
    databaseRestaurant: RestaurantEntity? = null,
    onEdit: () -> Unit = {},
    onSaveRating: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val restaurant = databaseRestaurant
    var rating by remember(restaurant?.id, restaurant?.rating) { mutableIntStateOf(restaurant?.rating ?: 0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val green = Color(0xFF60AD65)

    if (restaurant == null) {
        Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Restaurant not found", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { onNavigate("Home") }) { Text("Back to restaurants") }
        }
        return
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onNavigate("Home") }) { Text("Home") }
                TextButton(onClick = { onNavigate("Add") }) { Text("Add") }
                TextButton(onClick = { onNavigate("About") }) { Text("About") }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(restaurant.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(restaurant.cuisines, color = green)
            Text(restaurant.address)
            Text("Phone: ${restaurant.phone}")
            Text("Description", fontWeight = FontWeight.Bold)
            Text(restaurant.notes.ifBlank { "No description added." })
            Text("Your rating", fontWeight = FontWeight.Bold)
            Row {
                (1..5).forEach { star ->
                    TextButton(onClick = {
                        rating = star
                        onSaveRating?.invoke(star)
                    }) {
                        Text(if (star <= rating) "★" else "☆", fontSize = 30.sp, color = Color(0xFFFFA000))
                    }
                }
            }
            Button(onClick = onEdit, Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = green)) {
                Text("Edit Restaurant")
            }
            OutlinedButton(onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_SUBJECT, restaurant.name)
                    putExtra(Intent.EXTRA_TEXT, "${restaurant.name}\n${restaurant.address}\n${restaurant.phone}\n${restaurant.notes}\nTags: ${restaurant.cuisines}\nRating: $rating/5")
                }
                context.startActivity(Intent.createChooser(intent, "Share restaurant by email"))
            }, Modifier.fillMaxWidth()) { Text("Share by Email") }
            OutlinedButton(onClick = {
                val uri = Uri.parse("geo:0,0?q=${Uri.encode(restaurant.address)}")
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                } catch (_: Exception) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(restaurant.address)}")))
                }
            }, Modifier.fillMaxWidth()) { Text("View Full-Screen Map") }
            OutlinedButton(onClick = {
                val uri = Uri.parse("google.navigation:q=${Uri.encode(restaurant.address)}")
                val intent = Intent(Intent.ACTION_VIEW, uri).setPackage("com.google.android.apps.maps")
                try {
                    context.startActivity(intent)
                } catch (_: Exception) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${Uri.encode(restaurant.address)}")))
                }
            }, Modifier.fillMaxWidth()) { Text("Get Directions from Current Location") }
            OutlinedButton(onClick = { showDeleteDialog = true }, Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) {
                Text("Delete Restaurant")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete restaurant?") },
            text = { Text("Are you sure you want to delete ${restaurant.name}?") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; onDelete() }) { Text("Delete", color = Color.Red) }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}
