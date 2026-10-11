package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun AboutScreen(onNavigate: (String) -> Unit) {

    val green = Color(0xFF60AD65)

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
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(
                    R.drawable.restaurant_logo
                ),
                contentDescription = "Restaurant Guide Logo",
                modifier = Modifier.size(155.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Personal\nRestaurant Guide",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 29.sp,
                lineHeight = 35.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Discover & remember\nyour favourite places",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Version 1.0",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "A mobile app for food lovers.\n" +
                            "Find, save, and explore restaurants\n" +
                            "around you.",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Meet the team",
                modifier = Modifier.fillMaxWidth(),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            AboutTeamRow("Isaac")
            AboutTeamRow("Sanyoung")
            AboutTeamRow("Judene")

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun AboutTeamRow(name: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Person outline icon drawn using Compose
        Canvas(modifier = Modifier.size(28.dp)) {
            val lineWidth = 2.dp.toPx()

            drawCircle(
                color = Color.Black,
                radius = size.width * 0.17f,
                center = Offset(
                    size.width / 2,
                    size.height * 0.28f
                ),
                style = Stroke(width = lineWidth)
            )

            drawArc(
                color = Color.Black,
                startAngle = 195f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(
                    size.width * 0.12f,
                    size.height * 0.46f
                ),
                size = Size(
                    size.width * 0.76f,
                    size.height * 0.55f
                ),
                style = Stroke(width = lineWidth)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = name,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )

            Text(
                text = "Researcher & Developer",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    RestaurantGuideTheme {
        AboutScreen(onNavigate = {})
    }
}