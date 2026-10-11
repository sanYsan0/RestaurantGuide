package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// Colors
private val GreenAccent = Color(0xFF4CAF50)   // selected chip
private val GreenDark = Color(0xFF008000)     // FAB + active nav icon
private val ChipGray = Color(0xFFE8E8E8)
private val ScreenGray = Color(0xFFF4F4F4)

// ---------- Data ----------
data class Restaurant(
    val id: Int,
    val name: String,
    val cuisine: String,
    val tags: String,
    val rating: Double,
    val distance: String,
    val imageUrl: String,
    val position: LatLng
)

private val sampleRestaurants = listOf(
    Restaurant(
        1, "Piano Piano", "Italian", "Italian · Pasta", 5.0, "2.3 km",
        "https://example.com/piano_piano.jpg", LatLng(43.6677, -79.3948)
    ),
    Restaurant(
        2, "Sushi Kaji", "Japanese", "Japanese · Sushi", 4.7, "3.1 km",
        "https://example.com/sushi.jpg", LatLng(43.6720, -79.4050)
    ),
    Restaurant(
        3, "Pai", "Thai", "Thai · Noodles", 4.6, "1.8 km",
        "https://example.com/pai.jpg", LatLng(43.6500, -79.3900)
    ),
    Restaurant(
        4, "Terroni", "Italian", "Italian · Pizza", 4.5, "2.9 km",
        "https://example.com/terroni.jpg", LatLng(43.6620, -79.3830)
    )
)

private val filters = listOf("All", "Italian", "Japanese", "Thai")

// ---------- Screen ----------
@Composable
fun MapScreen(
    onBack: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onDirections: (Restaurant) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedId by remember { mutableStateOf<Int?>(1) }
    var bookmarked by remember { mutableStateOf(setOf<Int>()) }

    val visible = remember(query, selectedFilter) {
        sampleRestaurants.filter {
            (selectedFilter == "All" || it.cuisine == selectedFilter) &&
                    (query.isBlank() ||
                            it.name.contains(query, ignoreCase = true) ||
                            it.tags.contains(query, ignoreCase = true))
        }
    }
    val selected = visible.firstOrNull { it.id == selectedId }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(43.6677, -79.3948), 13f)
    }

    Scaffold(
        containerColor = ScreenGray,
        bottomBar = { BottomNav(onAddClick = onAddClick) }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Map fills the whole area
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraState,
                uiSettings = MapUiSettings(zoomControlsEnabled = false),
                onMapClick = { selectedId = null }
            ) {
                visible.forEach { r ->
                    MarkerInfoWindow(
                        state = remember(r.id) { MarkerState(position = r.position) },
                        icon = BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        ),
                        onClick = {
                            selectedId = r.id
                            false // let the info window show
                        }
                    ) {
                        CalloutBubble(r)
                    }
                }
            }

            // Header + search
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(bottom = 12.dp)
            ) {
                Header(onBack)
                SearchField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
                FilterRow(
                    selected = selectedFilter,
                    onSelect = { selectedFilter = it }
                )
            }

            // Bottom restaurant card
            if (selected != null) {
                RestaurantCard(
                    restaurant = selected,
                    isBookmarked = selected.id in bookmarked,
                    onBookmark = {
                        bookmarked = if (selected.id in bookmarked)
                            bookmarked - selected.id else bookmarked + selected.id
                    },
                    onDirections = { onDirections(selected) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(12.dp)
                )
            }
        }
    }
}

// Header
@Composable
private fun Header(onBack: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Text(
            "Map",
            fontFamily = FontFamily.Serif, // swap for a Google Font (e.g. DM Serif Display)
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp
        )
    }
}

// Search
@Composable
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFBDBDBD))
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Box(Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(
                        "Search restaurants by name or tag",
                        color = Color.Gray,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 13.sp, fontFamily = FontFamily.Monospace),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ---------- Filter chips ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterRow(selected: String, onSelect: (String) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filters) { label ->
            FilterChip(
                selected = label == selected,
                onClick = { onSelect(label) },
                label = {
                    Text(label, fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = ChipGray,
                    labelColor = Color.Black,
                    selectedContainerColor = GreenAccent,
                    selectedLabelColor = Color.White
                ),
                border = null
            )
        }
    }
}

// ---------- Map callout bubble ----------
@Composable
private fun CalloutBubble(r: Restaurant) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = r.imageUrl,
                contentDescription = r.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(ChipGray)
            )
            Spacer(Modifier.width(10.dp))
            Column {
                Text(r.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(
                    "${r.cuisine} · ${"%.1f".format(r.rating)}",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// ---------- Bottom restaurant card ----------
@Composable
private fun RestaurantCard(
    restaurant: Restaurant,
    isBookmarked: Boolean,
    onBookmark: () -> Unit,
    onDirections: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ChipGray)
            )
            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(restaurant.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(
                    restaurant.tags,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "%.1f".format(restaurant.rating),
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    restaurant.distance,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Gray
                )
            }

            // Directions button
            Surface(
                onClick = onDirections,
                shape = RoundedCornerShape(10.dp),
                color = ChipGray
            ) {
                Column(
                    Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Navigation,
                        contentDescription = "Directions",
                        tint = GreenDark
                    )
                    Text("Directions", fontSize = 8.sp, fontFamily = FontFamily.Monospace)
                }
            }

            IconButton(onClick = onBookmark) {
                Icon(
                    if (isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked) GreenDark else Color.Gray
                )
            }
        }
    }
}

// ---------- Bottom navigation with center "+" ----------
@Composable
private fun BottomNav(onAddClick: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 8.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { }) {
                // Active tab
                Icon(
                    Icons.Default.Place,
                    contentDescription = "Map",
                    tint = GreenDark
                )
            }
            FloatingActionButton(
                onClick = onAddClick,
                shape = CircleShape,
                containerColor = GreenDark,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Saved")
            }
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Info, contentDescription = "Info")
            }
        }
    }
}