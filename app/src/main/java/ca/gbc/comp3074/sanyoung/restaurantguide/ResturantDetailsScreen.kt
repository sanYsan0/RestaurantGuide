
package ca.gbc.comp3074.sanyoung.restaurantguide

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

data class RestaurantDetails(
    val name: String,
    val cuisine: String,
    val rating: String,
    val image: Int,
    val notes: String = ""
)

object RestaurantNotes {
    val savedNotes = mutableStateMapOf<String, String>()
}

object DeletedRestaurants {
    val restaurants = mutableStateMapOf<String, Boolean>()

    fun isDeleted(name: String): Boolean {
        return restaurants[name] == true
    }

    fun delete(name: String) {
        restaurants[name] = true
        SavedRestaurants.restaurants.remove(name)
        RestaurantNotes.savedNotes.remove(name)
    }
}

fun getRestaurantDetails(name: String): RestaurantDetails {
    return when (name) {
        "Piano Piano" -> RestaurantDetails(
            "Piano Piano",
            "Italian · Pasta",
            "5.0",
            R.drawable.piano_piano,
            "It was amazing!\n\nAbsolutely I will visit again :)"
        )

        "Fahrenheit Coffee" -> RestaurantDetails(
            "Fahrenheit Coffee",
            "Cafe · Coffee",
            "4.6",
            R.drawable.fahrenheit_coffee
        )

        "Pai Northern Thai" -> RestaurantDetails(
            "Pai Northern Thai",
            "Thai · Curry",
            "4.5",
            R.drawable.pai_northern_thai
        )

        "Imanishi" -> RestaurantDetails(
            "Imanishi",
            "Japanese",
            "4.6",
            R.drawable.imanishi
        )

        "Pai" -> RestaurantDetails(
            "Pai",
            "Thai · Noodles",
            "4.0",
            R.drawable.pai_northern_thai
        )

        "Sansotei Ramen" -> RestaurantDetails(
            "Sansotei Ramen",
            "Japanese · Ramen",
            "4.2",
            R.drawable.imanishi
        )

        else -> RestaurantDetails(
            name,
            "Restaurant",
            "0.0",
            R.drawable.restaurant_logo
        )
    }
}

@Composable
fun RestaurantDetailScreen(
    restaurantName: String = "Piano Piano",
    onNavigate: (String) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val green = Color(0xFF60AD65)
    val restaurant = getRestaurantDetails(restaurantName)

    val notes = RestaurantNotes.savedNotes[restaurantName]
        ?: restaurant.notes

    var editedNotes by remember(restaurantName) {
        mutableStateOf(notes)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var userRating by remember(restaurantName) {
        mutableIntStateOf(5)
    }

    val isSaved = SavedRestaurants.isSaved(restaurantName)

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
                                    if (item == "+") {
                                        onNavigate("Add")
                                    } else {
                                        onNavigate(item)
                                    }
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(restaurant.image),
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(235.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = restaurant.name,
                        fontFamily = FontFamily.Serif,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = if (isSaved) "♥" else "♡",
                        fontSize = 27.sp,
                        color = if (isSaved) {
                            Color.Red
                        } else {
                            Color.Gray
                        },
                        modifier = Modifier.clickable {
                            SavedRestaurants.toggle(restaurantName)
                        }
                    )
                }

                Text(
                    text = "${restaurant.cuisine}       ${restaurant.rating}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your rating",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    for (star in 1..5) {
                        Text(
                            text = if (star <= userRating) "★" else "☆",
                            fontSize = 24.sp,
                            color = Color(0xFF008000),
                            modifier = Modifier
                                .clickable { userRating = star }
                                .padding(horizontal = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Toronto",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Directions will be added later.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green
                        ),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("Directions", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Phone number not available.",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEAEAEA),
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("Call", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Restaurant: ${restaurant.name}"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "${restaurant.name}\n${restaurant.cuisine}\nToronto"
                                )
                            }

                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    "Share Restaurant"
                                )
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEAEAEA),
                            contentColor = Color.Black
                        ),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        Text("Share", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(restaurant.image),
                        contentDescription = "Restaurant Photo 1",
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )

                    Image(
                        painter = painterResource(restaurant.image),
                        contentDescription = "Restaurant Photo 2",
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Your Notes",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(7.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            1.dp,
                            Color.LightGray,
                            RoundedCornerShape(15.dp)
                        )
                        .padding(14.dp)
                ) {
                    Text(
                        text = if (notes.isBlank()) {
                            "No notes added yet."
                        } else {
                            notes
                        },
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        lineHeight = 20.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        editedNotes = notes
                        showEditDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green
                    )
                ) {
                    Text(
                        text = "✎ Edit Restaurant",
                        color = Color.Black,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = {
                        showDeleteDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEAEAEA)
                    )
                ) {
                    Text(
                        text = "Delete Restaurant",
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = {
                showEditDialog = false
            },
            title = {
                Text("Edit Your Notes")
            },
            text = {
                Column {
                    Text(
                        text = restaurant.name,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editedNotes,
                        onValueChange = {
                            editedNotes = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Your Notes")
                        },
                        minLines = 4,
                        maxLines = 7
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        RestaurantNotes.savedNotes[restaurantName] =
                            editedNotes
                        showEditDialog = false
                        Toast.makeText(
                            context,
                            "Notes updated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                ) {
                    Text("Save Notes", color = green)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Delete Restaurant?")
            },
            text = {
                Text(
                    "Are you sure you want to delete ${restaurant.name}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        DeletedRestaurants.delete(restaurantName)
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantDetailScreenPreview() {
    RestaurantGuideTheme {
        RestaurantDetailScreen(
            restaurantName = "Piano Piano",
            onNavigate = {},
            onDelete = {}
        )
    }
}
