package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.*

@Composable
fun ChunkyGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF4CAF50),
    bevelColor: Color = Color(0xFF2E7D32),
    borderColor: Color = PaperMarioColors.BorderBrown,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val alpha = if (enabled) 1f else 0.5f
    Box(
        modifier = modifier
            .clickable(enabled = enabled, onClick = onClick)
            .padding(bottom = 5.dp) // space for bevel
    ) {
        // Shadow/Bevel underneath
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 5.dp)
                .background(bevelColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .border(2.5.dp, borderColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
        )
        // Main button face
        Box(
            modifier = Modifier
                .background(containerColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .border(2.5.dp, borderColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val chapters by viewModel.allChapters.collectAsState()
    val progress by viewModel.userProgress.collectAsState()
    val starsCount = progress?.starsCount ?: 0
    val wildcardsCount = progress?.wildcardsCount ?: 0
    
    val narratorSpeed by viewModel.narratorSpeed.collectAsState()
    val narratorPitch by viewModel.narratorPitch.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF8D5A36), Color(0xFF3B1E0A)),
                    center = Offset(0.5f, 0.5f)
                )
            )
    ) {
        // 1. Vector background decoration: Cozy desk plank lines
        Canvas(modifier = Modifier.fillMaxSize()) {
            val woodColor = Color(0xFF1E0F07)
            val width = size.width
            val height = size.height
            
            // Wood plank horizontal joints
            for (y in listOf(0.15f, 0.35f, 0.55f, 0.75f, 0.95f)) {
                drawLine(
                    color = woodColor.copy(alpha = 0.3f),
                    start = Offset(0f, height * y),
                    end = Offset(width, height * y),
                    strokeWidth = 4f
                )
            }

            // Warm golden candle glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFF59D).copy(alpha = 0.25f), Color.Transparent),
                    center = Offset(width * 0.92f, height * 0.15f),
                    radius = width * 0.25f
                )
            )
        }

        if (isLandscape) {
            // LANDSCAPE: Classic Open Book Layout (Side by Side Pages)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .shadow(14.dp, RoundedCornerShape(20.dp))
                    .background(Color(0xFF703E1E), RoundedCornerShape(20.dp)) // Rich leather cover backing
                    .border(4.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(20.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFFDF2), RoundedCornerShape(16.dp)) // Ivory paper book pages
                        .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(16.dp))
                ) {
                    // LEFT PAGE (Mascot, Pocket Stats, Narrator Voice Panel)
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Title Logo
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFD54F), RoundedCornerShape(12.dp))
                                    .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = "✨ AUDIOAVENTURA ✨",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "Teatro de Cuentos Ilustrados 2D",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PaperMarioColors.BorderBrown.copy(alpha = 0.7f)
                            )
                        }

                        // Welcome Mascot
                        CardboardContainer(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White,
                            cornerRadius = 12.dp,
                            hasStitches = true
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(2.dp)
                            ) {
                                StickerCharacter(
                                    emoji = "🦝",
                                    size = 46.dp,
                                    hasPedestal = true
                                )
                                Column {
                                    Text(
                                        text = "¡Hola Aventurero!",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                    Text(
                                        text = "¡Narración con voz súper natural de cuento de hadas!",
                                        fontSize = 10.sp,
                                        color = PaperMarioColors.BorderBrown.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        // Stats Pocket Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEAF2F8), RoundedCornerShape(12.dp))
                                .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("⭐", fontSize = 20.sp)
                                Column {
                                    Text("Estrellas", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = PaperMarioColors.BorderBrown.copy(alpha = 0.5f))
                                    Text("$starsCount", fontSize = 14.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                            Box(modifier = Modifier.width(1.5.dp).height(20.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.15f)))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("🃏", fontSize = 20.sp)
                                Column {
                                    Text("Comodines", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = PaperMarioColors.BorderBrown.copy(alpha = 0.5f))
                                    Text("$wildcardsCount", fontSize = 14.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                        }

                        // Navigation Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ChunkyGameButton(
                                onClick = { viewModel.navigateTo(Screen.Characters) },
                                containerColor = Color(0xFFD32F2F),
                                bevelColor = Color(0xFF991B1B),
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Text("TIENDA 🎪", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                            ChunkyGameButton(
                                onClick = { viewModel.resetAllAppProgress() },
                                containerColor = Color(0xFFFFF9F2),
                                bevelColor = Color(0xFFDCD6CD),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("REINICIAR 🔄", fontSize = 10.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                            }
                        }
                    }

                    // BOOK SPINE CREASE
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(22.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x0C000000),
                                        Color(0x2A000000),
                                        Color(0x42000000),
                                        Color(0x2A000000),
                                        Color(0x0C000000),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(8) {
                                Box(modifier = Modifier.size(4.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.4f), CircleShape))
                            }
                        }
                    }

                    // RIGHT PAGE (Index List of Stories)
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1.1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📖 INDICE DE CUENTOS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = PaperMarioColors.BorderBrown,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color(0xFFFFFDF9), shape = RoundedCornerShape(14.dp))
                                .border(2.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(14.dp))
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chapters) { chapter ->
                                val isUnlocked = chapter.unlocked
                                val isCompleted = chapter.completed

                                val bg = if (isUnlocked) {
                                    when (chapter.id) {
                                        "ninja" -> Color(0xFFEBF5FB)
                                        "fantasma" -> Color(0xFFF5EEF8)
                                        "vaquero" -> Color(0xFFFEF9E7)
                                        "rey" -> Color(0xFFEAFAF1)
                                        "robot" -> Color(0xFFFDF2E9)
                                        else -> Color.White
                                    }
                                } else {
                                    Color(0xFFEBE5DB)
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(if (isUnlocked) 2.dp else 0.dp, RoundedCornerShape(12.dp))
                                        .background(bg, RoundedCornerShape(12.dp))
                                        .border(2.dp, if (isUnlocked) PaperMarioColors.BorderBrown else Color(0x33332011), RoundedCornerShape(12.dp))
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    StickerCharacter(
                                        emoji = if (isUnlocked) chapter.emoji else "🔒",
                                        size = 40.dp,
                                        hasPedestal = isUnlocked
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = chapter.title,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (isUnlocked) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.5f)
                                        )
                                        if (isUnlocked) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                repeat(5) { idx ->
                                                    Text(
                                                        text = if (idx < chapter.starsEarned) "⭐" else "☆",
                                                        fontSize = 10.sp,
                                                        color = if (idx < chapter.starsEarned) Color(0xFFFFC107) else Color(0x33332011)
                                                    )
                                                }
                                                if (isCompleted) {
                                                    Text(" • ✅ Completado", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27AE60))
                                                }
                                            }
                                        } else {
                                            Text("¡Completa el cuento anterior!", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7F8C8D))
                                        }
                                    }

                                    if (isUnlocked) {
                                        ChunkyGameButton(
                                            onClick = {
                                                viewModel.selectPreviewChapter(chapter.id)
                                                viewModel.navigateTo(Screen.StoryPreview)
                                            },
                                            containerColor = Color(0xFF4CAF50),
                                            bevelColor = Color(0xFF2E7D32),
                                            modifier = Modifier.width(72.dp)
                                        ) {
                                            Text("JUGAR ▶", fontSize = 8.5.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // PORTRAIT: Vertically Stacked Scrollable Notebook Layout (Fits Portrait Perfectly!)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .shadow(12.dp, RoundedCornerShape(18.dp))
                    .background(Color(0xFF703E1E), RoundedCornerShape(18.dp))
                    .border(3.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(18.dp))
                    .padding(3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFFDF2), RoundedCornerShape(14.dp))
                        .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(14.dp))
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Title Logo Banner
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFFD54F), RoundedCornerShape(12.dp))
                                .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "✨ AUDIOAVENTURA ✨",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = PaperMarioColors.BorderBrown,
                                letterSpacing = 1.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Teatro de Cuentos Ilustrados 2D",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.7f)
                        )
                    }

                    // Welcome Card
                    CardboardContainer(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.White,
                        cornerRadius = 12.dp,
                        hasStitches = true
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StickerCharacter(
                                emoji = "🦝",
                                size = 52.dp,
                                hasPedestal = true
                            )
                            Column {
                                Text(
                                    text = "¡Hola Aventurero!",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown
                                )
                                Text(
                                    text = "Elige un cuento para abrir su escenario de cartón interactivo.",
                                    fontSize = 11.sp,
                                    color = PaperMarioColors.BorderBrown.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Stats Dashboard
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEAF2F8), RoundedCornerShape(12.dp))
                            .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("⭐", fontSize = 24.sp)
                            Column {
                                Text("Estrellas", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PaperMarioColors.BorderBrown.copy(alpha = 0.5f))
                                Text("$starsCount", fontSize = 16.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                            }
                        }
                        Box(modifier = Modifier.width(2.dp).height(24.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.15f)))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("🃏", fontSize = 24.sp)
                            Column {
                                Text("Comodines", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PaperMarioColors.BorderBrown.copy(alpha = 0.5f))
                                Text("$wildcardsCount", fontSize = 16.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                            }
                        }
                    }

                    // Chapter List Index
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFFDF9), shape = RoundedCornerShape(14.dp))
                            .border(2.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(14.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📖 INDICE DE CUENTOS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = PaperMarioColors.BorderBrown,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        chapters.forEach { chapter ->
                            val isUnlocked = chapter.unlocked
                            val isCompleted = chapter.completed

                            val bg = if (isUnlocked) {
                                when (chapter.id) {
                                    "ninja" -> Color(0xFFEBF5FB)
                                    "fantasma" -> Color(0xFFF5EEF8)
                                    "vaquero" -> Color(0xFFFEF9E7)
                                    "rey" -> Color(0xFFEAFAF1)
                                    "robot" -> Color(0xFFFDF2E9)
                                    else -> Color.White
                                }
                            } else {
                                Color(0xFFEBE5DB)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(if (isUnlocked) 2.dp else 0.dp, RoundedCornerShape(12.dp))
                                    .background(bg, RoundedCornerShape(12.dp))
                                    .border(2.dp, if (isUnlocked) PaperMarioColors.BorderBrown else Color(0x33332011), RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StickerCharacter(
                                    emoji = if (isUnlocked) chapter.emoji else "🔒",
                                    size = 40.dp,
                                    hasPedestal = isUnlocked
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = chapter.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isUnlocked) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.5f)
                                    )
                                    if (isUnlocked) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            repeat(5) { idx ->
                                                Text(
                                                    text = if (idx < chapter.starsEarned) "⭐" else "☆",
                                                    fontSize = 10.sp,
                                                    color = if (idx < chapter.starsEarned) Color(0xFFFFC107) else Color(0x33332011)
                                                )
                                            }
                                            if (isCompleted) {
                                                Text(" • ✅ ¡Leído!", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27AE60))
                                            }
                                        }
                                    } else {
                                        Text("¡Completa el cuento anterior!", fontSize = 9.sp, color = Color(0xFF7F8C8D))
                                    }
                                }

                                if (isUnlocked) {
                                    ChunkyGameButton(
                                        onClick = {
                                            viewModel.selectPreviewChapter(chapter.id)
                                            viewModel.navigateTo(Screen.StoryPreview)
                                        },
                                        containerColor = Color(0xFF4CAF50),
                                        bevelColor = Color(0xFF2E7D32),
                                        modifier = Modifier.width(76.dp)
                                    ) {
                                        Text("JUGAR ▶", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    // Action buttons (Shop and Reset) at bottom
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChunkyGameButton(
                            onClick = { viewModel.navigateTo(Screen.Characters) },
                            containerColor = Color(0xFFD32F2F),
                            bevelColor = Color(0xFF991B1B),
                            modifier = Modifier.weight(1.2f)
                        ) {
                            Text("TIENDA DE COMUNIDAD 🎪", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        ChunkyGameButton(
                            onClick = { viewModel.resetAllAppProgress() },
                            containerColor = Color(0xFFFFF9F2),
                            bevelColor = Color(0xFFDCD6CD),
                            modifier = Modifier.weight(0.9f)
                        ) {
                            Text("REINICIAR progress", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PaperMarioColors.BorderBrown)
                        }
                    }
                }
            }
        }
    }
}
