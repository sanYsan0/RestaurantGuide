package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.gbc.comp3074.sanyoung.restaurantguide.ui.theme.RestaurantGuideTheme

@Composable
fun HomeScreen(
    onSeeAll: () -> Unit,
    onAdd: () -> Unit,
    onRestaurantClick: (String) -> Unit,
    onNavigate: (String) -> Unit
) {
    val green = Color(0xFF60AD65)

    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = Color.White,

        // The button and navigation stay at the bottom.
        bottomBar = {
            Surface(color = Color.White) {
                Column(
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    Button(
                        onClick = onAdd,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 8.dp
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green
                        )
                    ) {
                        Text("+ Add Restaurants")
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                            onAdd()
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
                    top = 24.dp,
                    bottom = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(
                            R.drawable.restaurant_logo
                        ),
                        contentDescription = "Restaurant Guide logo",
                        modifier = Modifier.size(110.dp),
                        contentScale = ContentScale.Fit
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Personal\nRestaurant Guide",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            lineHeight = 31.sp,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Discover & remember\nyour favourite places",
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

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
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "My Restaurants",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                TextButton(onClick = onSeeAll) {
                    Text(
                        text = "See all",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            val showPiano = matchesHomeSearch(
                "Piano Piano", "Italian · Pasta", search, category
            )
            val showCoffee = matchesHomeSearch(
                "Fahrenheit Coffee", "Cafe · Coffee", search, category
            )
            val showPai = matchesHomeSearch(
                "Pai Northern Thai", "Thai · Curry", search, category
            )
            val showImanishi = matchesHomeSearch(
                "Imanishi", "Japanese", search, category
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
                    rating = "4.6",
                    imageRes = R.drawable.fahrenheit_coffee,
                    onClick = {
                        onRestaurantClick("Fahrenheit Coffee")
                    }
                )
            }

            if (showPai) {
                RestaurantCard(
                    name = "Pai Northern Thai",
                    tags = "Thai · Curry",
                    rating = "4.5",
                    imageRes = R.drawable.pai_northern_thai,
                    onClick = {
                        onRestaurantClick("Pai Northern Thai")
                    }
                )
            }

            if (showImanishi) {
                RestaurantCard(
                    name = "Imanishi",
                    tags = "Japanese",
                    rating = "4.6",
                    imageRes = R.drawable.imanishi,
                    onClick = { onRestaurantClick("Imanishi") }
                )
            }

            if (!showPiano && !showCoffee && !showPai && !showImanishi) {
                Text(
                    text = "No restaurants found.",
                    color = Color.Gray
                )
            }
        }
    }
}

private fun matchesHomeSearch(
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
fun HomeScreenPreview() {
    RestaurantGuideTheme {
        HomeScreen(
            onSeeAll = {},
            onAdd = {},
            onRestaurantClick = {},
            onNavigate = {}
        )
    }
}