package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
fun StoryPreviewScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val chapters by viewModel.allChapters.collectAsState()
    val activeChapterId by viewModel.activeChapterId.collectAsState()
    val progress by viewModel.userProgress.collectAsState()
    val starsCount = progress?.starsCount ?: 0
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Find the currently selected chapter or default to the first one
    val currentIdx = remember(chapters, activeChapterId) {
        val index = chapters.indexOfFirst { it.id == activeChapterId }
        if (index != -1) index else 0
    }
    
    val currentChapter = remember(chapters, currentIdx) {
        if (chapters.isNotEmpty() && currentIdx in chapters.indices) {
            chapters[currentIdx]
        } else null
    }

    // Dynamic color backgrounds for each chapter to give them a unique popup flavor
    val chapterThemeColor = remember(currentChapter) {
        currentChapter?.let {
            when (it.id) {
                "ninja" -> Color(0xFFEBF5FB)
                "fantasma" -> Color(0xFFF5EEF8)
                "vaquero" -> Color(0xFFFEF9E7)
                "rey" -> Color(0xFFEAFAF1)
                "robot" -> Color(0xFFFDF2E9)
                else -> Color.White
            }
        } ?: Color.White
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Layout container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // HEADER BAR: "CUENTOS DE AVENTURA" with thick borders and playful game controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Actions: Settings Back button (Cog icon) and Speaker TTS button
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cog-styled Back button
                    ChunkyGameButton(
                        onClick = {
                            viewModel.stopSpeaking()
                            viewModel.navigateTo(Screen.Home)
                        },
                        containerColor = Color(0xFF3498DB),
                        bevelColor = Color(0xFF2980B9),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Text(
                            text = "⚙️",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Narrator Speaker toggle button
                    ChunkyGameButton(
                        onClick = {
                            currentChapter?.let {
                                if (isSpeaking) {
                                    viewModel.stopSpeaking()
                                } else {
                                    viewModel.speakText("Capítulo ${currentIdx + 1}: ${it.title}. ${it.description}")
                                }
                            }
                        },
                        containerColor = if (isSpeaking) Color(0xFFE74C3C) else Color(0xFF2ECC71),
                        bevelColor = if (isSpeaking) Color(0xFFC0392B) else Color(0xFF27AE60),
                        modifier = Modifier.size(54.dp)
                    ) {
                        Text(
                            text = if (isSpeaking) "🔇" else "🔊",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Middle Banner: "CUENTOS DE AVENTURA"
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF39C12), RoundedCornerShape(12.dp))
                        .border(3.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                        .padding(horizontal = 24.dp, vertical = 6.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "CUENTOS DE AVENTURA",
                        fontSize = if (isLandscape) 18.sp else 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                }

                // Right Actions: User profile icon with total stars count
                Box(
                    modifier = Modifier
                        .background(Color(0xFF3498DB), RoundedCornerShape(12.dp))
                        .border(3.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                        .size(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }

            // SKEUOMORPHIC OPENED BOOK LAYOUT
            if (currentChapter != null) {
                if (isLandscape) {
                    // LANDSCAPE MODE: Book lying open side-by-side + Mascot on the right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // SKEUOMORPHIC OPENED BOOK
                        Row(
                            modifier = Modifier
                                .weight(3.2f)
                                .fillMaxHeight()
                                .shadow(12.dp, RoundedCornerShape(20.dp))
                                .background(Color(0xFF703E1E), RoundedCornerShape(20.dp)) // Leather cover back
                                .border(4.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(20.dp))
                                .padding(4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFFFFDF2), RoundedCornerShape(16.dp)) // Ivory paper pages
                                    .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(16.dp))
                            ) {
                                // LEFT PAGE: CHAPTER SUMMARY & TITLE
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                        .border(
                                            width = 1.dp,
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.15f)
                                        )
                                        .padding(16.dp)
                                ) {
                                    // Scroll ornament frame
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val sizeX = size.width
                                        val sizeY = size.height
                                        val strokeWidth = 3f
                                        val offsetMargin = 10f
                                        
                                        // Left top corner swirl
                                        drawArc(
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                            startAngle = 180f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            topLeft = Offset(offsetMargin, offsetMargin),
                                            size = Size(30f, 30f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                                        )
                                        // Right top corner swirl
                                        drawArc(
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                            startAngle = 270f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            topLeft = Offset(sizeX - 30f - offsetMargin, offsetMargin),
                                            size = Size(30f, 30f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                                        )
                                        // Bottom left
                                        drawArc(
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                            startAngle = 90f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            topLeft = Offset(offsetMargin, sizeY - 30f - offsetMargin),
                                            size = Size(30f, 30f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                                        )
                                        // Bottom right
                                        drawArc(
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                            startAngle = 0f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            topLeft = Offset(sizeX - 30f - offsetMargin, sizeY - 30f - offsetMargin),
                                            size = Size(30f, 30f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                                        )
                                    }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        // Castle drawing & book metadata
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text("🏰", fontSize = 18.sp)
                                                Text(
                                                    text = "Cuentos de Aventura",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = PaperMarioColors.BorderBrown
                                                )
                                                Text("🌲", fontSize = 18.sp)
                                            }
                                            Text(
                                                text = "◆ Capítulo ${currentIdx + 1} ◆",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFD35400)
                                            )
                                        }

                                        // Headline
                                        Text(
                                            text = "BIENVENIDO AL\nCAPÍTULO ${currentIdx + 1}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Black,
                                            color = PaperMarioColors.BorderBrown,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 18.sp
                                        )

                                        // Scrollable Synopsis Content
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(vertical = 4.dp)
                                                .verticalScroll(rememberScrollState()),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = currentChapter.description,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = PaperMarioColors.BorderBrown,
                                                lineHeight = 16.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        // Decorative footer divider
                                        Text(
                                            text = "❧  ❦  ☙",
                                            fontSize = 10.sp,
                                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.4f)
                                        )
                                    }
                                }

                                // RIGHT PAGE: INTERACTIVE LEVEL PREVIEW & CHAPTER SELECTOR
                                Column(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "PRIMER PÁGINA:\nLA HISTORIA COMIENZA",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown,
                                        textAlign = TextAlign.Center
                                    )

                                    // Level Badge with arrows to swap chapters
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        // Left Arrow
                                        ChunkyGameButton(
                                            onClick = {
                                                if (currentIdx > 0) {
                                                    viewModel.stopSpeaking()
                                                    viewModel.selectPreviewChapter(chapters[currentIdx - 1].id)
                                                }
                                            },
                                            containerColor = Color(0xFFF1C40F),
                                            bevelColor = Color(0xFFD68910),
                                            enabled = currentIdx > 0,
                                            modifier = Modifier.size(42.dp)
                                        ) {
                                            Text("◀", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        }

                                        // Main Badge Panel
                                        Box(
                                            modifier = Modifier
                                                .size(width = 120.dp, height = 95.dp)
                                                .background(
                                                    if (currentChapter.unlocked) Color(0xFF2C3E50) else Color(0xFF7F8C8D),
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                                .shadow(4.dp, RoundedCornerShape(12.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(2.dp)
                                            ) {
                                                Text(
                                                    text = "NIVEL ${currentIdx + 1}",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White
                                                )
                                                
                                                // Lock / Unlock symbol
                                                Box(modifier = Modifier.size(32.dp)) {
                                                    com.example.ui.components.CharacterGraphic(
                                                        emoji = if (currentChapter.unlocked) currentChapter.emoji else "🔒"
                                                    )
                                                }

                                                Text(
                                                    text = if (currentChapter.unlocked) "DESBLOQUEADO" else "BLOQUEADO",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (currentChapter.unlocked) Color(0xFF2ECC71) else Color(0xFFE74C3C)
                                                )
                                            }
                                        }

                                        // Right Arrow
                                        ChunkyGameButton(
                                            onClick = {
                                                if (currentIdx < chapters.size - 1) {
                                                    viewModel.stopSpeaking()
                                                    viewModel.selectPreviewChapter(chapters[currentIdx + 1].id)
                                                }
                                            },
                                            containerColor = Color(0xFFF1C40F),
                                            bevelColor = Color(0xFFD68910),
                                            enabled = currentIdx < chapters.size - 1,
                                            modifier = Modifier.size(42.dp)
                                        ) {
                                            Text("▶", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                        }
                                    }

                                    // Chapter Stars Score representation (Completed / Uncompleted)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Render 3 stars representing completed score
                                        repeat(3) { idx ->
                                            val isGold = currentChapter.completed && idx < (currentChapter.starsEarned / 1.7f).toInt().coerceAtLeast(1)
                                            Text(
                                                text = "★",
                                                fontSize = 22.sp,
                                                color = if (isGold) Color(0xFFF1C40F) else Color(0xFFBDC3C7)
                                            )
                                        }
                                    }

                                    // Large Play Button: "COMENZAR HISTORIA" or "BLOQUEADO"
                                    ChunkyGameButton(
                                        onClick = {
                                            if (currentChapter.unlocked) {
                                                viewModel.startChapter(currentChapter.id)
                                                viewModel.navigateTo(Screen.Story)
                                            }
                                        },
                                        containerColor = if (currentChapter.unlocked) Color(0xFFE67E22) else Color(0xFFBDC3C7),
                                        bevelColor = if (currentChapter.unlocked) Color(0xFFD35400) else Color(0xFF95A5A6),
                                        enabled = currentChapter.unlocked,
                                        modifier = Modifier.fillMaxWidth(0.95f)
                                    ) {
                                        Text(
                                            text = if (currentChapter.unlocked) "COMENZAR HISTORIA" else "COMPLETAR ANTERIOR",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // COZY MASCOT SIDECOLUMN (fits perfectly on right in Landscape)
                        Column(
                            modifier = Modifier
                                .weight(1.1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 4.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Speech bubble 1
                                Box(
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = if (currentChapter.unlocked) {
                                            "¡HOLA AVENTURERO! PARA EMPEZAR LA HISTORIA PULSA EN EL BOTÓN GRANDE."
                                        } else {
                                            "¡HOLA! ESTE CAPÍTULO ESTÁ BLOQUEADO. ¡VENCE LOS ANTERIORES PARA PASAR!"
                                        },
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown,
                                        textAlign = TextAlign.Start
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Speech bubble 2
                                Box(
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(12.dp))
                                        .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = "¡ASÍ PROGRESARÁS EN LA HISTORIA Y DESBLOQUEARÁS MÁS PERSONAJES!",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Robot Mascot (Sticker style with pedestal)
                            StickerCharacter(
                                emoji = "🤖",
                                size = 80.dp,
                                hasPedestal = true
                            )
                        }
                    }
                } else {
                    // PORTRAIT MODE: Pages vertically stacked scrollable (Fills Portrait perfectly)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .shadow(10.dp, RoundedCornerShape(16.dp))
                            .background(Color(0xFF703E1E), RoundedCornerShape(16.dp))
                            .border(3.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(16.dp))
                            .padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFFFDF2), RoundedCornerShape(12.dp))
                                .border(2.2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // TITLE SECTION
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("🏰", fontSize = 18.sp)
                                    Text(
                                        text = "Cuentos de Aventura",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PaperMarioColors.BorderBrown
                                    )
                                    Text("🌲", fontSize = 18.sp)
                                }
                                Text(
                                    text = "◆ Capítulo ${currentIdx + 1} ◆",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD35400)
                                )
                                Text(
                                    text = currentChapter.title,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            // DESCRIPTION CARD
                            CardboardContainer(
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = chapterThemeColor,
                                cornerRadius = 12.dp,
                                hasStitches = false
                            ) {
                                Text(
                                    text = currentChapter.description,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = PaperMarioColors.BorderBrown,
                                    lineHeight = 18.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            // CHAPTER NAVIGATION ROW
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Left Arrow
                                ChunkyGameButton(
                                    onClick = {
                                        if (currentIdx > 0) {
                                            viewModel.stopSpeaking()
                                            viewModel.selectPreviewChapter(chapters[currentIdx - 1].id)
                                        }
                                    },
                                    containerColor = Color(0xFFF1C40F),
                                    bevelColor = Color(0xFFD68910),
                                    enabled = currentIdx > 0,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Text("◀", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }

                                // Badge
                                Box(
                                    modifier = Modifier
                                        .size(width = 110.dp, height = 90.dp)
                                        .background(
                                            if (currentChapter.unlocked) Color(0xFF2C3E50) else Color(0xFF7F8C8D),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "NIVEL ${currentIdx + 1}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Black,
                                            color = Color.White
                                        )
                                        Box(modifier = Modifier.size(32.dp)) {
                                            com.example.ui.components.CharacterGraphic(
                                                emoji = if (currentChapter.unlocked) currentChapter.emoji else "🔒"
                                            )
                                        }
                                        Text(
                                            text = if (currentChapter.unlocked) "DESBLOQUEADO" else "BLOQUEADO",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (currentChapter.unlocked) Color(0xFF2ECC71) else Color(0xFFE74C3C)
                                        )
                                    }
                                }

                                // Right Arrow
                                ChunkyGameButton(
                                    onClick = {
                                        if (currentIdx < chapters.size - 1) {
                                            viewModel.stopSpeaking()
                                            viewModel.selectPreviewChapter(chapters[currentIdx + 1].id)
                                        }
                                    },
                                    containerColor = Color(0xFFF1C40F),
                                    bevelColor = Color(0xFFD68910),
                                    enabled = currentIdx < chapters.size - 1,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Text("▶", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }

                            // Stars Score representation
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(3) { idx ->
                                    val isGold = currentChapter.completed && idx < (currentChapter.starsEarned / 1.7f).toInt().coerceAtLeast(1)
                                    Text(
                                        text = "★",
                                        fontSize = 22.sp,
                                        color = if (isGold) Color(0xFFF1C40F) else Color(0xFFBDC3C7)
                                    )
                                }
                            }

                            // Action button
                            ChunkyGameButton(
                                onClick = {
                                    if (currentChapter.unlocked) {
                                        viewModel.startChapter(currentChapter.id)
                                        viewModel.navigateTo(Screen.Story)
                                    }
                                },
                                containerColor = if (currentChapter.unlocked) Color(0xFFE67E22) else Color(0xFFBDC3C7),
                                bevelColor = if (currentChapter.unlocked) Color(0xFFD35400) else Color(0xFF95A5A6),
                                enabled = currentChapter.unlocked,
                                modifier = Modifier.fillMaxWidth(0.95f)
                            ) {
                                Text(
                                    text = if (currentChapter.unlocked) "COMENZAR HISTORIA" else "COMPLETAR ANTERIOR",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // MASCOT / ROBOT STICKER & SPEECH BUBBLES FOR PORTRAIT ONLY
            if (!isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Speech bubbles on the left
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Speech bubble 1
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = if (currentChapter?.unlocked == true) {
                                    "¡HOLA AVENTURERO! PARA EMPEZAR LA HISTORIA PULSA EN EL BOTÓN GRANDE."
                                } else {
                                    "¡HOLA! ESTE CAPÍTULO ESTÁ BLOQUEADO. ¡VENCE LOS ANTERIORES PARA PASAR!"
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = PaperMarioColors.BorderBrown,
                                textAlign = TextAlign.Start
                            )
                        }

                        // Speech bubble 2
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(2.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(12.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "¡ASÍ PROGRESARÁS EN LA HISTORIA Y DESBLOQUEARÁS MÁS PERSONAJES!",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = PaperMarioColors.BorderBrown,
                                textAlign = TextAlign.Start
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Robot Mascot (Sticker style with pedestal)
                    StickerCharacter(
                        emoji = "🤖",
                        size = 72.dp,
                        hasPedestal = true
                    )
                }
            }
        }
    }
}
