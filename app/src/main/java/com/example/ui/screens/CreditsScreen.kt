package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.StampFont
import kotlinx.coroutines.delay

@Composable
fun CreditsScreen(viewModel: MainViewModel) {
    var stage by remember { mutableStateOf(0) } // 0: Blank, 1: Image 1, 2: Loading/Done
    LaunchedEffect(Unit) {
        delay(500) // Initial blank screen
        stage = 1 // Show Image 1
        delay(2500) // Keep Image 1 visible
        stage = 2 // Hide Image 1 and show loading
        delay(1500) // Wait showing loading
        viewModel.navigateTo(Screen.Home)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF844E37)),
        contentAlignment = Alignment.Center
    ) {
        // Image 1
        AnimatedVisibility(
            visible = stage == 1,
            enter = fadeIn(animationSpec = tween(800)),
            exit = fadeOut(animationSpec = tween(800))
        ) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val resId = try {
                val id = context.resources.getIdentifier("creditos_1", "drawable", context.packageName)
                if (id != 0) id else android.R.drawable.ic_menu_gallery
            } catch (e: Exception) {
                android.R.drawable.ic_menu_gallery
            }
            
            Image(
                painter = painterResource(id = resId),
                contentDescription = "Créditos 1",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Loading display
        AnimatedVisibility(
            visible = stage == 2,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
            ) {
                Text(
                    text = "Cargando aventuras...",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontFamily = StampFont,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        
        // Show instruction text if placeholder is used
        if (stage == 1) {
            val context = androidx.compose.ui.platform.LocalContext.current
            val id = context.resources.getIdentifier("creditos_1", "drawable", context.packageName)
            if (id == 0) {
                Text(
                    text = "Falta imagen: Por favor sube 'creditos_1.png'\na la carpeta app/src/main/res/drawable/",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
