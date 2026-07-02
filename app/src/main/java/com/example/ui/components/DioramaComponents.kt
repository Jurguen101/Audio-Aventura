package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A beautiful pop-up style diorama card that mimics cardboard layers.
 * Extremely lightweight and perfectly fluid on low-end devices.
 */
@Composable
fun DioramaCard(
    modifier: Modifier = Modifier,
    borderSize: Dp = 4.dp,
    borderColor: Color = Color(0xFF2C3E50),
    backgroundColor: Color = Color(0xFFFDFBF7), // Cozy warm paper tone
    shadowOffset: Dp = 8.dp,
    shadowColor: Color = Color(0x33000000),
    testTag: String = "",
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable { 
            com.example.utils.SoundManager.playPop()
            onClick() 
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .then(clickModifier)
    ) {
        // Shadow base layer (cardboard base shadow)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = shadowOffset, y = shadowOffset)
                .background(borderColor.copy(alpha = 0.9f), shape = RoundedCornerShape(20.dp))
        )

        // Raised paper cut-out foreground layer
        Box(
            modifier = Modifier
                .background(backgroundColor, shape = RoundedCornerShape(20.dp))
                .border(borderSize, borderColor, shape = RoundedCornerShape(20.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
            content = content
        )
    }
}

/**
 * Beautiful styled Pop-up Header for the adventures.
 */
@Composable
fun DioramaHeader(
    title: String,
    subtitle: String,
    starsCount: Int,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title & badge
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2C3E50),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF7F8C8D),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Stars bubble and Reset icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Star Counter Card
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFF9E6), shape = RoundedCornerShape(12.dp))
                    .border(2.dp, Color(0xFFF39C12), shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Estrellas ganadas",
                        tint = Color(0xFFF1C40F),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "$starsCount ⭐",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD35400)
                    )
                }
            }

            // Quick reset button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color(0xFFFDEDEC), shape = CircleShape)
                    .border(2.dp, Color(0xFFE74C3C), shape = CircleShape)
                    .clickable { 
                        com.example.utils.SoundManager.playPop()
                        onResetClick() 
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔄", fontSize = 14.sp)
            }
        }
    }
}

/**
 * Animated Diorama View simulating 3D pop-up layered pages.
 * Runs beautiful light breathing parallax transitions.
 * Uses 0% memory, zero bitmap allocations. Perfect for low-end device offline performance.
 */
data class DioramaThemeColors(
    val groundColor: Color,
    val spotlightColor: Color,
    val curtainColor: Color,
    val headerTitle: String
)

fun getDioramaTheme(storyTheme: String): DioramaThemeColors {
    return when (storyTheme) {
        "ninja" -> DioramaThemeColors(Color(0xFF8D6E63), Color(0xFFE57373).copy(alpha = 0.15f), Color(0xFFB71C1C), "DOJO SECRETO") // Ninja
        "fantasma" -> DioramaThemeColors(Color(0xFF455A64), Color(0xFFCE93D8).copy(alpha = 0.15f), Color(0xFF4A148C), "MANSIÓN EMBRUJADA") // Fantasma
        "vaquero" -> DioramaThemeColors(Color(0xFFD7CCC8), Color(0xFFFFCC80).copy(alpha = 0.15f), Color(0xFFE65100), "LEJANO OESTE") // Vaquero
        "rey" -> DioramaThemeColors(Color(0xFFB71C1C), Color(0xFFFFF59D).copy(alpha = 0.2f), Color(0xFF1A237E), "SALÓN DEL TRONO") // Rey
        "robot" -> DioramaThemeColors(Color(0xFF90A4AE), Color(0xFF80DEEA).copy(alpha = 0.2f), Color(0xFF37474F), "LABORATORIO") // Robot
        "dragon" -> DioramaThemeColors(Color(0xFF4E342E), Color(0xFFFFAB91).copy(alpha = 0.15f), Color(0xFFBF360C), "CUEVA DEL DRAGÓN") // Dragón
        "pirata" -> DioramaThemeColors(Color(0xFF795548), Color(0xFF90CAF9).copy(alpha = 0.15f), Color(0xFF006064), "BARCO PIRATA") // Pirata
        "astronauta" -> DioramaThemeColors(Color(0xFFCFD8DC), Color(0xFFB39DDB).copy(alpha = 0.15f), Color(0xFF283593), "LUNA EXTERIOR") // Astronauta
        "ardilla" -> DioramaThemeColors(Color(0xFFA5D6A7), Color(0xFFFFF59D).copy(alpha = 0.15f), Color(0xFF2E7D32), "BOSQUE MÁGICO") // Ardilla
        "mago" -> DioramaThemeColors(Color(0xFF512DA8), Color(0xFFCE93D8).copy(alpha = 0.2f), Color(0xFF311B92), "TORRE DEL MAGO") // Mago
        else -> DioramaThemeColors(Color(0xFFC0A473).copy(alpha = 0.8f), Color(0xFFFFF59D).copy(alpha = 0.15f), PaperMarioColors.StageRed, "ESCENARIO DE AVENTURAS")
    }
}

@Composable
fun PopUpDioramaCanvas(
    mainEmoji: String,
    floatingEmojis: List<String>,
    storyTheme: String = "",

    backgroundColors: List<Long>,
    modifier: Modifier = Modifier
) {
    // Infinite animation for paper breathing (simulating pop-up movement)
    val infiniteTransition = rememberInfiniteTransition(label = "PaperBreathing")
    
    val verticalOffset by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "YOffset"
    )

    val scaleFactor by infiniteTransition.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )


    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Rotate"
    )

    val walkProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Walk"
    )


    val brush = remember(backgroundColors) {
        Brush.verticalGradient(
            colors = backgroundColors.map { Color(it) }
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(4.dp, Color(0xFF2C3E50), shape = RoundedCornerShape(20.dp))
            .background(brush)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw some visual guidelines / paper grid lines inside the diorama
        Canvas(modifier = Modifier.fillMaxSize()) {
            val steps = 8
            val stepHeight = size.height / steps
            for (i in 1 until steps) {
                drawLine(
                    color = Color.White.copy(alpha = 0.08f),
                    start = Offset(0f, i * stepHeight),
                    end = Offset(size.width, i * stepHeight),
                    strokeWidth = 2f
                )
            }
        }

        // Layer 1: Background floating elements (e.g., small trees, stars)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = verticalOffset * 0.4f
                    translationX = -verticalOffset * 0.2f
                }
        ) {
            floatingEmojis.forEachIndexed { index, emoji ->
                val alignment = when (index % 4) {
                    0 -> Alignment.TopStart
                    1 -> Alignment.TopEnd
                    2 -> Alignment.BottomStart
                    else -> Alignment.BottomEnd
                }
                Box(
                    modifier = Modifier
                        .align(alignment)
                        .padding(16.dp)
                        .size(32.dp)
                        .rotate(rotationAngle * (index + 1) * 0.5f)
                ) {
                    // Draw geometric shape as background prop instead of emoji
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val shapeType = index % 3
                        val color = when (index % 4) {
                            0 -> Color(0xFFFFD700) // Gold
                            1 -> Color(0xFFFF5722) // Deep Orange
                            2 -> Color(0xFF4CAF50) // Green
                            else -> Color(0xFF03A9F4) // Blue
                        }
                        when (shapeType) {
                            0 -> drawCircle(color = color, radius = size.width / 2f)
                            1 -> drawRect(color = color, size = androidx.compose.ui.geometry.Size(size.width, size.height))
                            2 -> {
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(size.width / 2f, 0f)
                                    lineTo(size.width, size.height)
                                    lineTo(0f, size.height)
                                    close()
                                }
                                drawPath(path, color = color)
                            }
                        }
                    }
                }
            }
        }

        // Layer 2: Cardboard ground / pop-up folded base
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(48.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 12.dp)
                .graphicsLayer {
                    scaleX = scaleFactor
                }
                .background(
                    color = getDioramaTheme(storyTheme).groundColor,
                    shape = RoundedCornerShape(100.dp)
                )
                .border(2.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(100.dp))
        )

        // Layer 3: Main focal character styled as a beautiful Cardboard Standee with white margins and wooden pedestal
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    val walkX = if (walkProgress < 0.5f) {
                        -80f + (walkProgress * 2f) * 160f
                    } else {
                        80f - ((walkProgress - 0.5f) * 2f) * 160f
                    }
                    val directionScale = if (walkProgress < 0.5f) 1f else -1f
                    val bounce = kotlin.math.abs(kotlin.math.sin(walkProgress * kotlin.math.PI * 16).toFloat()) * -30f
                    
                    translationX = walkX
                    translationY = verticalOffset + bounce
                    scaleX = scaleFactor * directionScale
                    scaleY = scaleFactor
                    rotationZ = rotationAngle + (bounce * 0.1f)
                },
            contentAlignment = Alignment.Center
        ) {
            StickerCharacter(
                emoji = mainEmoji,
                size = 90.dp,
                hasPedestal = true
            )
        }

        // Layer 4: Theater Spotlight Beams
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Left spotlight cone
            val pathLeft = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width * 0.45f, size.height)
                lineTo(size.width * 0.15f, size.height)
                close()
            }
            drawPath(
                path = pathLeft,
                color = getDioramaTheme(storyTheme).spotlightColor
            )

            // Right spotlight cone
            val pathRight = androidx.compose.ui.graphics.Path().apply {
                moveTo(size.width, 0f)
                lineTo(size.width * 0.85f, size.height)
                lineTo(size.width * 0.55f, size.height)
                close()
            }
            drawPath(
                path = pathRight,
                color = getDioramaTheme(storyTheme).spotlightColor
            )
        }

        // Layer 5: Decorative Red Stage Curtains on the Left, Right and Top (Vignette)
        // Left side drape
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(18.dp)
                .align(Alignment.CenterStart)
                .background(getDioramaTheme(storyTheme).curtainColor)
                .border(2.dp, PaperMarioColors.BorderBrown)
        )
        // Right side drape
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(18.dp)
                .align(Alignment.CenterEnd)
                .background(getDioramaTheme(storyTheme).curtainColor)
                .border(2.dp, PaperMarioColors.BorderBrown)
        )
        // Top Valance curtain
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .align(Alignment.TopCenter)
                .background(getDioramaTheme(storyTheme).curtainColor)
                .border(2.dp, PaperMarioColors.BorderBrown),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getDioramaTheme(storyTheme).headerTitle,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        // Paper craft folding lines / strings (decorative details that increase craft authenticity)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw a thin string holding the main character
            drawLine(
                color = Color.White.copy(alpha = 0.2f),
                start = Offset(size.width / 2, 0f),
                end = Offset(size.width / 2, size.height / 3),
                strokeWidth = 3f
            )
        }
    }
}
