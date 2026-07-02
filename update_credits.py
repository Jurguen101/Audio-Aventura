import re

with open("app/src/main/java/com/example/ui/screens/CreditsScreen.kt", "w") as f:
    f.write("""package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import kotlinx.coroutines.delay

@Composable
fun CreditsScreen(viewModel: MainViewModel) {
    LaunchedEffect(Unit) {
        delay(6000)
        viewModel.navigateTo(Screen.Home)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top part (75%)
        Box(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxWidth()
                .background(Color(0xFFFA9A55))
                .padding(48.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Creador de la Idea:",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "   Juan Carlos Mairena Avilez  (UNAN - Managua)",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Programador General:",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "   Jurguen Adriel Delgadillo Jarquin  (UNI)",
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }

        // Bottom part (25%)
        Box(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth()
                .background(Color(0xFFF97A1F))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Universidad Nacional de Ingeniería",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Universidad Nacional Autonoma de Nicaragua - Managua",
                    fontSize = 20.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
""")
