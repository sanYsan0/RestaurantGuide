
package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SavedRestaurants {
    val restaurants = mutableStateMapOf<String, Boolean>()

    fun isSaved(name: String): Boolean {
        return restaurants[name] == true
    }

    fun toggle(name: String) {
        restaurants[name] = !isSaved(name)
    }
}

@Composable
fun RestaurantCard(
    name: String,
    tags: String,
    rating: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    if (DeletedRestaurants.isDeleted(name)) {
        return
    }

    val saved = SavedRestaurants.isSaved(name)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color(0xFFFAFAFA)
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 10.dp,
                        end = 36.dp,
                        bottom = 10.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(imageRes),
                    contentDescription = "$name photo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = name,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = tags,
                        color = Color.Gray,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )

                    Text(
                        text = "★ $rating",
                        color = Color(0xFFFFA000),
                        fontSize = 11.sp
                    )

                    Text(
                        text = "Toronto",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }

            IconButton(
                onClick = {
                    SavedRestaurants.toggle(name)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(3.dp)
                    .size(36.dp)
            ) {
                if (saved) {
                    Canvas(modifier = Modifier.size(18.dp)) {
                        val bookmark = Path().apply {
                            moveTo(size.width * 0.2f, size.height * 0.1f)
                            lineTo(size.width * 0.8f, size.height * 0.1f)
                            lineTo(size.width * 0.8f, size.height * 0.95f)
                            lineTo(size.width * 0.5f, size.height * 0.72f)
                            lineTo(size.width * 0.2f, size.height * 0.95f)
                            close()
                        }

                        drawPath(
                            path = bookmark,
                            color = Color.Red
                        )
                    }
                } else {
                    Icon(
                        painter = painterResource(R.drawable.ic_bookmark),
                        contentDescription = "Save $name",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
