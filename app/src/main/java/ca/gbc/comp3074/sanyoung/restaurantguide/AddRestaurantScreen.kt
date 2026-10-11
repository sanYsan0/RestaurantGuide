
package ca.gbc.comp3074.sanyoung.restaurantguide

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

data class NewRestaurant(
    val name: String,
    val cuisines: List<String>,
    val rating: Int,
    val address: String,
    val phone: String,
    val notes: String,
    val photo: String
)

object AddedRestaurants {
    val restaurants = mutableStateListOf<NewRestaurant>()
}

@Composable
fun AddRestaurantScreen(
    onNavigate: (String) -> Unit,
    onSave: (NewRestaurant) -> Unit
) {
    val context = LocalContext.current
    val green = Color(0xFF60AD65)
    val cuisines = listOf(
        "Italian", "Japanese", "Cafe", "Thai",
        "Vegan", "Sweets", "Other"
    )

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    var photo by remember { mutableStateOf<Uri?>(null) }
    var showErrors by remember { mutableStateOf(false) }
    val selectedCuisines = remember {
        mutableStateListOf<String>()
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        photo = uri
    }

    fun saveRestaurant() {
        if (
            name.isBlank() ||
            address.isBlank() ||
            phone.isBlank() ||
            selectedCuisines.isEmpty()
        ) {
            showErrors = true
            Toast.makeText(
                context,
                "Please fill in the required fields.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            onSave(
                NewRestaurant(
                    name = name.trim(),
                    cuisines = selectedCuisines.toList(),
                    rating = rating,
                    address = address.trim(),
                    phone = phone.trim(),
                    notes = notes.trim(),
                    photo = photo?.toString() ?: ""
                )
            )
        }
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Surface(color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 4.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Home", "Map", "+", "Saved", "About")
                        .forEach { item ->
                            TextButton(
                                onClick = {
                                    onNavigate(
                                        if (item == "+") "Add" else item
                                    )
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = item,
                                    color = green,
                                    fontSize = 14.sp
                                )
                            }
                        }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { onNavigate("Home") }
                ) {
                    Text(
                        "‹",
                        fontSize = 30.sp,
                        color = Color.Black
                    )
                }

                Text(
                    "Add Restaurant",
                    fontFamily = FontFamily.Serif,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Button(
                    onClick = {
                        val message = "$name\n$address\n$notes"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, message)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Share Restaurant"
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp
                    )
                ) {
                    Text("Share", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Restaurant Name *",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text("e.g. Kinton Ramen", fontSize = 12.sp)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && name.isBlank(),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Add Photo",
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp
            )

            Text(
                "(optional)",
                color = Color.Gray,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                onClick = {
                    photoPicker.launch("image/*")
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(135.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF2F2F2),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color.LightGray
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("▣", fontSize = 24.sp)

                    Text(
                        if (photo == null) {
                            "Choose Photo"
                        } else {
                            "Photo Selected"
                        },
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Cuisine / Tag *",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cuisines.chunked(4).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        row.forEach { cuisine ->
                            val selected =
                                selectedCuisines.contains(cuisine)

                            Surface(
                                onClick = {
                                    if (selected) {
                                        selectedCuisines.remove(cuisine)
                                    } else {
                                        selectedCuisines.add(cuisine)
                                    }
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = if (selected) {
                                    green
                                } else {
                                    Color(0xFFEAEAEA)
                                }
                            ) {
                                Text(
                                    text = cuisine,
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 9.dp
                                    ),
                                    color = if (selected) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    },
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Your Rating",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Row {
                for (star in 1..5) {
                    Text(
                        text = "★",
                        fontSize = 30.sp,
                        color = if (star <= rating) {
                            Color(0xFF008000)
                        } else {
                            Color.LightGray
                        },
                        modifier = Modifier
                            .clickable { rating = star }
                            .padding(end = 10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                "Location *",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = {
                    Text(
                        "📍 Search or enter address...",
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && address.isBlank(),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Phone Number *",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = {
                    Text(
                        "e.g. +1 437 - 250 - 2538",
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = showErrors && phone.isBlank(),
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                "Notes",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = {
                    Text(
                        "Add notes about this restaurant...",
                        fontSize = 12.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                maxLines = 5,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { saveRestaurant() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green
                )
            ) {
                Text("Save Restaurant")
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRestaurantScreenPreview() {
    RestaurantGuideTheme {
        AddRestaurantScreen(
            onNavigate = {},
            onSave = {}
        )
    }
}
