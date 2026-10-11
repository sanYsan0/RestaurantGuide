
package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

@Composable
fun RestaurantListScreen(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val green = Color(0xFF60AD65)

    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }

    BackHandler(onBack = onBack)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Surface(color = Color.White) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Home", "Map", "+", "Saved", "About")
                        .forEach { item ->
                            TextButton(
                                onClick = {
                                    when (item) {
                                        "Home" -> onBack()
                                        "+" -> onAdd()
                                        else -> onNavigate(item)
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
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 20.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.width(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "‹",
                        fontSize = 32.sp,
                        color = Color.Black
                    )
                }

                Text(
                    text = "Restaurants",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                placeholder = {
                    Text(
                        text = "Search restaurants by name or tag",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = green,
                    unfocusedBorderColor = Color(0xFFD0D0D0),
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "Italian", "Japanese", "Thai")
                    .forEach { label ->
                        val selected = category == label

                        Surface(
                            onClick = { category = label },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(24.dp),
                            color = if (selected) {
                                green
                            } else {
                                Color(0xFFEAEAEA)
                            }
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(
                                    vertical = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                color = if (selected) {
                                    Color.White
                                } else {
                                    Color.DarkGray
                                }
                            )
                        }
                    }
            }

            Spacer(modifier = Modifier.height(6.dp))

            val showPiano = SavedRestaurants.isSaved("Piano Piano") &&
                    matchesListSearch(
                        "Piano Piano", "Italian · Pasta", search, category
                    )

            val showCoffee = SavedRestaurants.isSaved("Fahrenheit Coffee") &&
                    matchesListSearch(
                        "Fahrenheit Coffee", "Cafe · Coffee", search, category
                    )

            val showNorthernThai = SavedRestaurants.isSaved("Pai Northern Thai") &&
                    matchesListSearch(
                        "Pai Northern Thai", "Thai · Curry", search, category
                    )

            val showImanishi = SavedRestaurants.isSaved("Imanishi") &&
                    matchesListSearch(
                        "Imanishi", "Japanese · Ramen", search, category
                    )

            val showPai = SavedRestaurants.isSaved("Pai") &&
                    matchesListSearch(
                        "Pai", "Thai · Noodles", search, category
                    )

            val showSansotei = SavedRestaurants.isSaved("Sansotei Ramen") &&
                    matchesListSearch(
                        "Sansotei Ramen", "Japanese · Ramen", search, category
                    )

            if (showPiano) {
                RestaurantCard(
                    name = "Piano Piano",
                    tags = "Italian · Pasta",
                    rating = "5.0",
                    imageRes = R.drawable.piano_piano,
                    onClick = { onRestaurantClick("Piano Piano") }
                )
            }

            if (showCoffee) {
                RestaurantCard(
                    name = "Fahrenheit Coffee",
                    tags = "Cafe · Coffee",
                    rating = "4.5",
                    imageRes = R.drawable.fahrenheit_coffee,
                    onClick = {
                        onRestaurantClick("Fahrenheit Coffee")
                    }
                )
            }

            if (showNorthernThai) {
                RestaurantCard(
                    name = "Pai Northern Thai",
                    tags = "Thai · Curry",
                    rating = "4.8",
                    imageRes = R.drawable.pai_northern_thai,
                    onClick = {
                        onRestaurantClick("Pai Northern Thai")
                    }
                )
            }

            if (showImanishi) {
                RestaurantCard(
                    name = "Imanishi",
                    tags = "Japanese · Ramen",
                    rating = "4.0",
                    imageRes = R.drawable.imanishi,
                    onClick = { onRestaurantClick("Imanishi") }
                )
            }

            if (showPai) {
                RestaurantCard(
                    name = "Pai",
                    tags = "Thai · Noodles",
                    rating = "4.0",
                    imageRes = R.drawable.pai_northern_thai,
                    onClick = { onRestaurantClick("Pai") }
                )
            }

            if (showSansotei) {
                RestaurantCard(
                    name = "Sansotei Ramen",
                    tags = "Japanese · Ramen",
                    rating = "4.2",
                    imageRes = R.drawable.imanishi,
                    onClick = {
                        onRestaurantClick("Sansotei Ramen")
                    }
                )
            }

            if (
                !showPiano &&
                !showCoffee &&
                !showNorthernThai &&
                !showImanishi &&
                !showPai &&
                !showSansotei
            ) {
                Text(
                    text = if (SavedRestaurants.restaurants.values.none { it }) {
                        "No saved restaurants yet. Bookmark a restaurant on Home."
                    } else {
                        "No saved restaurants match your search."
                    },
                    color = Color.Gray
                )
            }
        }
    }
}

private fun matchesListSearch(
    name: String,
    tags: String,
    search: String,
    category: String
): Boolean {
    val matchesSearch =
        name.contains(search.trim(), ignoreCase = true) ||
                tags.contains(search.trim(), ignoreCase = true)

    val matchesCategory =
        category == "All" ||
                tags.contains(category, ignoreCase = true)

    return matchesSearch && matchesCategory
}

@Preview(showBackground = true)
@Composable
fun RestaurantListScreenPreview() {
    RestaurantGuideTheme {
        RestaurantListScreen(
            onBack = {},
            onAdd = {},
            onRestaurantClick = {},
            onNavigate = {}
        )
    }
}
