
package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

@Composable
fun MapScreen(
    onBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val green = Color(0xFF60AD65)
    var search by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("All") }

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
                                        onAddClick()
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
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Text(
                    text = "Map",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = {
                    Text(
                        "Search restaurants by name or tag",
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = green,
                    unfocusedBorderColor = Color(0xFFD0D0D0),
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
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

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                StaticMapBackground()

                Text(
                    text = "THE ANNEX",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60718A),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 35.dp, y = (-120).dp)
                )

                Text(
                    text = "BLOOR-YORKVILLE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60718A),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(y = (-55).dp)
                )

                Text(
                    text = "TORONTO",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60718A),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 30.dp, y = 95.dp)
                )

                Icon(
                    Icons.Default.Place,
                    contentDescription = "Restaurant marker",
                    tint = Color(0xFF008000),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 70.dp, y = (-65).dp)
                        .size(28.dp)
                )

                Icon(
                    Icons.Default.Place,
                    contentDescription = "Restaurant marker",
                    tint = Color(0xFF008000),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = (-55).dp, y = 65.dp)
                        .size(28.dp)
                )

                Icon(
                    Icons.Default.Place,
                    contentDescription = "Restaurant marker",
                    tint = Color(0xFF008000),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-55).dp, y = 55.dp)
                        .size(28.dp)
                )

                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = Color.White,
                    shadowElevation = 5.dp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-25).dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(
                                R.drawable.piano_piano
                            ),
                            contentDescription = "Piano Piano",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                "Piano Piano",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                "Italian · 5.0",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(
                                R.drawable.piano_piano
                            ),
                            contentDescription = "Piano Piano",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(65.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Piano Piano",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )

                            Text(
                                "Italian · Pasta",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )

                            Text(
                                "★★★★★  5.0",
                                fontSize = 12.sp,
                                color = Color(0xFFFFA500)
                            )

                            Text(
                                "2.3 km",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Navigation,
                                contentDescription = "Directions",
                                tint = Color(0xFF008000)
                            )

                            Text(
                                "Directions",
                                fontSize = 9.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            Icons.Outlined.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StaticMapBackground() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2EFE7))
    ) {
        val w = size.width
        val h = size.height

        drawRect(
            color = Color(0xFFD0E7D0),
            topLeft = Offset(w * 0.65f, 0f),
            size = Size(w * 0.35f, h * 0.45f)
        )

        drawRect(
            color = Color(0xFFB8DDBB),
            topLeft = Offset(w * 0.08f, h * 0.55f),
            size = Size(w * 0.20f, h * 0.18f)
        )

        for (i in 0..20) {
            val x = i * w / 18f

            drawLine(
                color = Color.White,
                start = Offset(x, 0f),
                end = Offset(x + w * 0.12f, h),
                strokeWidth = 7f
            )
        }

        for (i in 0..25) {
            val y = i * h / 23f

            drawLine(
                color = Color.White,
                start = Offset(0f, y),
                end = Offset(w, y - h * 0.15f),
                strokeWidth = 7f
            )
        }

        drawLine(
            color = Color(0xFFF1C979),
            start = Offset(w * 0.25f, 0f),
            end = Offset(w * 0.62f, h),
            strokeWidth = 16f
        )

        drawLine(
            color = Color(0xFF98D5E8),
            start = Offset(w * 0.90f, 0f),
            end = Offset(w * 0.70f, h * 0.45f),
            strokeWidth = 12f
        )
    }
}
