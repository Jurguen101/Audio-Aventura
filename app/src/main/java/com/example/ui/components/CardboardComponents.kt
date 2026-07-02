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

// Color Palette for Paper Mario Cardboard theme
object PaperMarioColors {
    val CardboardTan = Color(0xFFE5CE9F)      // Classic craft paper base
    val CardboardDark = Color(0xFFC0A473)     // Darker cardboard shading
    val BorderBrown = Color(0xFF332011)       // Outlines resembling heavy ink/pencil
    val PaperWhite = Color(0xFFFFFDF7)        // Bright textured paper
    val SkyBlue = Color(0xFF86D5F8)           // Vivid sky blue
    val GrassGreen = Color(0xFF76C447)        // Storybook grass
    val StageRed = Color(0xFFD32F2F)          // Theater stage curtain red
    val BannerYellow = Color(0xFFFFD54F)      // Bright stars/bannering
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
    Box(
        modifier = modifier
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

@Composable
fun PaperCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = PaperMarioColors.PaperWhite,
    borderColor: Color = PaperMarioColors.BorderBrown,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color.Black.copy(alpha = 0.3f)
            )
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(14.dp)
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
                .padding(4.dp)
        ) {
            Text(
                text = emoji,
                fontSize = (size.value * 0.55f).sp,
                textAlign = TextAlign.Center
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
