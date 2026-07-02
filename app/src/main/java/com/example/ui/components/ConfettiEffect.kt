package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.isActive
import kotlin.random.Random

data class ConfettiParticle(
    var x: Float,
    var y: Float,
    var speedX: Float,
    var speedY: Float,
    var color: Color,
    var size: Float,
    var rotation: Float,
    var rotationSpeed: Float
)

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit
) {
    val particles = remember {
        List(150) {
            ConfettiParticle(
                x = 0.5f,
                y = 0.5f,
                speedX = Random.nextFloat() * 30f - 15f,
                speedY = -(Random.nextFloat() * 25f + 10f),
                color = listOf(
                    Color(0xFFE74C3C), Color(0xFFF1C40F), Color(0xFF3498DB),
                    Color(0xFF2ECC71), Color(0xFF9B59B6), Color(0xFFE67E22), Color.White
                ).random(),
                size = Random.nextFloat() * 20f + 10f,
                rotation = Random.nextFloat() * 360f,
                rotationSpeed = Random.nextFloat() * 30f - 15f
            )
        }
    }

    var ticks by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (isActive && System.currentTimeMillis() - startTime < 3500) {
            withFrameMillis { 
                ticks++ 
            }
        }
        onComplete()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        if (ticks == 0L) return@Canvas
        
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Initialize positions on first tick
        if (ticks == 1L) {
            particles.forEach {
                it.x = canvasWidth / 2f
                it.y = canvasHeight / 2f
            }
        }

        particles.forEach { p ->
            p.x += p.speedX
            p.y += p.speedY
            p.speedY += 0.8f // Gravity
            p.rotation += p.rotationSpeed

            rotate(degrees = p.rotation, pivot = Offset(p.x, p.y)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(p.x - p.size / 2, p.y - p.size / 2),
                    size = Size(p.size, p.size)
                )
            }
        }
    }
}
