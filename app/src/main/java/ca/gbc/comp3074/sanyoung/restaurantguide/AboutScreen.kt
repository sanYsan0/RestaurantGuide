package ca.gbc.comp3074.sanyoung.restaurantguide

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen(onNavigate: (String) -> Unit) {
    Scaffold(
        bottomBar = {
            Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = { onNavigate("Home") }) { Text("Home") }
                TextButton(onClick = { onNavigate("Add") }) { Text("Add") }
                TextButton(onClick = { onNavigate("List") }) { Text("Restaurants") }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Text("About", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Personal Restaurant Guide")
            Text("Team Members", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Isaac Natera")
            Text("Judene Brown")
            Text("Sanyoung Yoon")
        }
    }
}
