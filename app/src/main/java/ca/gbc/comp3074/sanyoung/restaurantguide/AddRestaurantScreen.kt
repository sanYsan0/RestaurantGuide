package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class NewRestaurant(
    val name: String,
    val cuisines: List<String>,
    val rating: Int,
    val address: String,
    val phone: String,
    val notes: String,
    val photo: String = "",
    val id: Int? = null
)

@Composable
fun AddRestaurantScreen(
    onNavigate: (String) -> Unit,
    onSave: (NewRestaurant) -> Unit,
    initialRestaurant: RestaurantEntity? = null
) {
    var name by remember(initialRestaurant?.id) { mutableStateOf(initialRestaurant?.name ?: "") }
    var address by remember(initialRestaurant?.id) { mutableStateOf(initialRestaurant?.address ?: "") }
    var phone by remember(initialRestaurant?.id) { mutableStateOf(initialRestaurant?.phone ?: "") }
    var description by remember(initialRestaurant?.id) { mutableStateOf(initialRestaurant?.notes ?: "") }
    var rating by remember(initialRestaurant?.id) { mutableIntStateOf(initialRestaurant?.rating ?: 0) }
    var tagsText by remember(initialRestaurant?.id) { mutableStateOf(initialRestaurant?.cuisines ?: "") }
    var newTag by remember { mutableStateOf("") }
    var showErrors by remember { mutableStateOf(false) }
    val green = Color(0xFF60AD65)
    val tags = tagsText.split(",").map { it.trim() }.filter { it.isNotBlank() }.distinct()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(12.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onNavigate("Home") }) { Text("Cancel") }
                Button(onClick = {
                    if (name.isBlank() || address.isBlank() || phone.isBlank() || tags.isEmpty() || rating !in 1..5) {
                        showErrors = true
                    } else {
                        onSave(NewRestaurant(name.trim(), tags, rating, address.trim(), phone.trim(), description.trim(), id = initialRestaurant?.id))
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = green)) {
                    Text(if (initialRestaurant == null) "Save Restaurant" else "Save Changes")
                }
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(if (initialRestaurant == null) "Add Restaurant" else "Edit Restaurant", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Restaurant name *") }, isError = showErrors && name.isBlank(), singleLine = true)
            OutlinedTextField(address, { address = it }, Modifier.fillMaxWidth(), label = { Text("Address *") }, isError = showErrors && address.isBlank())
            OutlinedTextField(phone, { phone = it }, Modifier.fillMaxWidth(), label = { Text("Phone number *") }, isError = showErrors && phone.isBlank(), singleLine = true)
            OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), label = { Text("Description") }, minLines = 3)
            Text("Tags *", fontWeight = FontWeight.Bold)
            Text(if (tags.isEmpty()) "No tags added." else tags.joinToString(" · "), color = Color.Gray)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(newTag, { newTag = it }, Modifier.weight(1f), label = { Text("Add a tag") }, singleLine = true)
                Button(onClick = {
                    if (newTag.isNotBlank() && tags.none { it.equals(newTag.trim(), true) }) {
                        tagsText = (tags + newTag.trim()).joinToString(", ")
                    }
                    newTag = ""
                }, colors = ButtonDefaults.buttonColors(containerColor = green)) { Text("Add") }
            }
            if (showErrors && tags.isEmpty()) Text("Add at least one tag.", color = MaterialTheme.colorScheme.error)
            Text("Your rating *", fontWeight = FontWeight.Bold)
            Row {
                (1..5).forEach { star ->
                    TextButton(onClick = { rating = star }) {
                        Text(if (star <= rating) "★" else "☆", fontSize = 30.sp, color = Color(0xFFFFA000))
                    }
                }
            }
            if (showErrors && rating !in 1..5) Text("Choose a rating from 1 to 5.", color = MaterialTheme.colorScheme.error)
        }
    }
}
