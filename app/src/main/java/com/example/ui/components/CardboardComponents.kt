package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.LayoutDirection

// Color Palette for Paper Mario Cardboard theme (Updated to Kraft Aesthetic)
object PaperMarioColors {
    val CardboardTan = Color(0xFFD4B595)      // Kraft Base
    val CardboardDark = Color(0xFFA67F59)     // Darker kraft shading / corrugated shadow
    val BorderBrown = Color(0xFF1E1E1E)       // Ink black for stamps and borders
    val PaperWhite = Color(0xFFE9E4D4)        // Recycled paper white
    val SkyBlue = Color(0xFF86D5F8)           // Vivid sky blue (kept for contrast)
    val GrassGreen = Color(0xFF76C447)        // Storybook grass (kept for contrast)
    val StageRed = Color(0xFF9E3636)          // Stamp red
    val BannerYellow = Color(0xFFF2C94C)      // Muted yellow
}

@Composable
fun CardboardContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PaperMarioColors.CardboardTan,
    borderColor: Color = PaperMarioColors.BorderBrown,
    cornerRadius: Dp = 16.dp,
    hasStitches: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val rotationX by animateFloatAsState(
        targetValue = if (visible) 0f else -60f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
                this.rotationX = rotationX
                cameraDistance = 12 * density
            }
            // Drop shadow for that layered cutout look
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.Black.copy(alpha = 0.4f),
                spotColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(backgroundColor, RoundedCornerShape(cornerRadius))
            .border(3.dp, borderColor, RoundedCornerShape(cornerRadius))
            .drawBehind {
                if (hasStitches) {
                    val stroke = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                    )
                    drawRoundRect(
                        color = borderColor.copy(alpha = 0.6f),
                        topLeft = Offset(12f, 12f),
                        size = this.size.copy(
                            width = this.size.width - 24f,
                            height = this.size.height - 24f
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                            (cornerRadius.toPx() - 12f).coerceAtLeast(0f)
                        ),
                        style = stroke
                    )
                }
            }
            .padding(if (hasStitches) 16.dp else 12.dp)
    ) {
        content()
    }
}


class TornPaperShape(private val roughness: Float = 10f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            
            // Torn bottom edge
            var currentX = size.width
            var goingUp = false
            while (currentX > 0) {
                currentX -= roughness
                if (currentX < 0) currentX = 0f
                val y = if (goingUp) size.height - roughness else size.height
                lineTo(currentX, y)
                goingUp = !goingUp
            }
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun PaperCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PaperMarioColors.PaperWhite,
    borderColor: Color = PaperMarioColors.BorderBrown,
    content: @Composable ColumnScope.() -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val rotationX by animateFloatAsState(
        targetValue = if (visible) 0f else -60f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    Column(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
                this.rotationX = rotationX
                cameraDistance = 12 * density
            }
            .shadow(
                elevation = 6.dp,
                shape = TornPaperShape(roughness = 12f),
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            .background(backgroundColor, TornPaperShape(roughness = 12f))
            .border(2.dp, borderColor, TornPaperShape(roughness = 12f))
            .padding(14.dp)
            .padding(bottom = 8.dp) // extra padding for the torn edge
    ) {
        content()
    }
}

@Composable
fun StickerCharacter(
    emoji: String,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    label: String? = null,
    hasPedestal: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        // Sticker cutout body
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                // Layer 1: Sticker Shadow
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size / 3),
                    clip = false
                )
                // Layer 2: White paper cutout background
                .background(Color.White, RoundedCornerShape(size / 3))
                // Layer 3: Thick stylized black pencil border
                .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(size / 3))
                .padding(12.dp) // Adjusted padding for CharacterGraphic
        ) {
            CharacterGraphic(
                emoji = emoji,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (hasPedestal) {
            // Cute wooden cardboard stand underneath, just like Paper Mario characters
            Box(
                modifier = Modifier
                    .width(size * 0.9f)
                    .height(8.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp))
                    .background(Color(0xFF8B5A2B), RoundedCornerShape(2.dp)) // Wood brown stand
                    .border(1.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(2.dp))
            )
        }

        if (label != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = PaperMarioColors.BorderBrown,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(PaperMarioColors.PaperWhite, RoundedCornerShape(4.dp))
                    .border(1.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun CardboardButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = PaperMarioColors.BannerYellow,
    borderColor: Color = PaperMarioColors.BorderBrown,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val alpha = if (enabled) 1f else 0.5f
    Box(
        modifier = modifier
            .shadow(
                elevation = if (enabled) 4.dp else 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = containerColor.copy(alpha = alpha),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.5.dp,
                color = borderColor.copy(alpha = alpha),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = enabled, onClick = {
                com.example.utils.SoundManager.playPop()
                onClick()
            })
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}

@Composable
fun PaperBanner(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = PaperMarioColors.StageRed,
    textColor: Color = Color.White
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, shape = RoundedCornerShape(4.dp))
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(4.dp))
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ScissorCutDecoration(
    modifier: Modifier = Modifier,
    color: Color = PaperMarioColors.BorderBrown
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("✂️ - - - - - - - - - - - - - - - - - - -", color = color.copy(alpha = 0.5f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AnimatedCardboardPopup(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delayMillis.toLong())
        visible = true
    }

    val rotationX by animateFloatAsState(
        targetValue = if (visible) 0f else -75f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
        label = "rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "alpha"
    )

    Box(
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            this.scaleX = scale
            this.scaleY = scale
            this.rotationX = rotationX
            cameraDistance = 8 * density
        }
    ) {
        content()
    }
}

@Composable
fun DioramaAppWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(Color(0xFF8D5A36), Color(0xFF3B1E0A)),
                    center = Offset(0.5f, 0.5f)
                )
            )
    ) {
        // Wooden desk texture
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val woodColor = Color(0xFF1E0F07)
            val width = size.width
            val height = size.height
            for (y in listOf(0.15f, 0.35f, 0.55f, 0.75f, 0.95f)) {
                drawLine(
                    color = woodColor.copy(alpha = 0.3f),
                    start = Offset(0f, height * y),
                    end = Offset(width, height * y),
                    strokeWidth = 4f
                )
            }
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .graphicsLayer {
                    
                    
                    scaleX = 0.98f
                    scaleY = 0.98f
                    
                },
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
