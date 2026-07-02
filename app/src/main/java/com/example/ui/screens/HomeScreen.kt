package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.StoryChapter
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.*
import com.example.data.UserProgress
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val chapters by viewModel.allChapters.collectAsState()
    val progress by viewModel.userProgress.collectAsState()
    val characters by viewModel.allCharacters.collectAsState()
    val starsCount = progress?.starsCount ?: 0
    val wildcardsCount = progress?.wildcardsCount ?: 0
    val activePetId = progress?.activePetId
    val activePet = characters.find { it.id == activePetId }

    val totalPages = if (chapters.isNotEmpty()) chapters.size + 1 else 0
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()
    var showResetWarning by remember { mutableStateOf(false) }
    var selectedChapterForSynopsis by remember { mutableStateOf<StoryChapter?>(null) }

    if (selectedChapterForSynopsis != null) {
        val chapter = selectedChapterForSynopsis!!
        AlertDialog(
            onDismissRequest = { selectedChapterForSynopsis = null },
            title = { Text(text = chapter.title, fontWeight = FontWeight.Bold, color = PaperMarioColors.StageRed) },
            text = { Text(text = chapter.description, color = PaperMarioColors.BorderBrown) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.startChapter(chapter.id)
                    viewModel.navigateTo(Screen.Story)
                    selectedChapterForSynopsis = null
                }) {
                    Text("Aceptar", color = PaperMarioColors.StageRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedChapterForSynopsis = null }) {
                    Text("Declinar", color = PaperMarioColors.BorderBrown, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PaperMarioColors.PaperWhite
        )
    }

    if (showResetWarning) {
        AlertDialog(
            onDismissRequest = { showResetWarning = false },
            title = { Text(text = "⚠️ Atención", fontWeight = FontWeight.Bold, color = PaperMarioColors.StageRed) },
            text = { Text(text = "¿Estás seguro de que deseas borrar todo el progreso? Esto reiniciará todas tus estrellas, mascotas y niveles desbloqueados.", color = PaperMarioColors.BorderBrown) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllAppProgress()
                    showResetWarning = false
                }) {
                    Text("Reiniciar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetWarning = false }) {
                    Text("Cancelar", color = PaperMarioColors.BorderBrown, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PaperMarioColors.PaperWhite
        )
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Open Book Layout
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Book cover / pages
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(16.dp, RoundedCornerShape(16.dp))
                    .background(PaperMarioColors.PaperWhite, RoundedCornerShape(16.dp))
            ) {
                // Left page: Assistant & Stats
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Top Stats
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    androidx.compose.material3.Icon(Icons.Filled.Star, contentDescription="Star", tint=Color(0xFFFFD700), modifier=Modifier.size(24.dp))
                                    Text("$starsCount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    androidx.compose.material3.Icon(Icons.Filled.StarHalf, contentDescription="Wildcard", tint=Color(0xFF9C27B0), modifier=Modifier.size(24.dp))
                                    Text("$wildcardsCount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                        }

                        // Beautiful Tienda Button
                        ChunkyGameButton(
                            onClick = { viewModel.navigateTo(Screen.Characters) },
                            containerColor = Color(0xFFF1C40F),
                            bevelColor = Color(0xFFF39C12),
                            borderColor = Color(0xFFD35400),
                            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                androidx.compose.material3.Icon(Icons.Filled.ShoppingCart, contentDescription="Store", tint=Color.White, modifier=Modifier.size(28.dp).padding(end = 12.dp))
                                Text("TIENDA DE MASCOTAS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                        }

                        AchievementsPanel(progress = progress ?: UserProgress(), chapters = chapters, characters = characters)

                        // Virtual Assistant
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (activePet != null) {
                                CardboardContainer(
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    backgroundColor = Color.White
                                ) {
                                    Text(
                                        text = "¡Hola! Desliza para explorar los cuentos y pulsa JUGAR.",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PaperMarioColors.BorderBrown,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                StickerCharacter(
                                    emoji = activePet.emoji,
                                    size = 120.dp,
                                    hasPedestal = true,
                                    modifier = Modifier.clickable {
                                        com.example.utils.SoundManager.playPop()
                                        viewModel.speakText(activePet.soundPhrase)
                                    }
                                )
                            }
                        }
                    }
                }

                // Book Spine
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(24.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0x1A000000),
                                    Color(0x33000000),
                                    Color(0x4D000000),
                                    Color(0x33000000),
                                    Color(0x1A000000),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(10) {
                            Box(modifier = Modifier.size(6.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.5f), CircleShape))
                        }
                    }
                }
                // Right page: The Diorama Pager
                Box(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                        .padding(16.dp)
                ) {
                    if (chapters.isNotEmpty()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            if (page < chapters.size) {
                                val chapter = chapters[page]
                                DioramaPage(
                                    chapter = chapter,
                                    onPlayClick = {
                                        selectedChapterForSynopsis = chapter
                                    }
                                )
                            } else {
                                val allCompleted = chapters.isNotEmpty() && chapters.all { it.completed }
                                CreditsTriggerPage(
                                    allCompleted = allCompleted,
                                    onTriggerClick = { viewModel.navigateTo(Screen.FinalCredits) }
                                )
                            }
                        }
                    }
                }
            }
            
            // Pager Indicators
            if (chapters.isNotEmpty()) {
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(totalPages) { iteration ->
                        val color = if (pagerState.currentPage == iteration) PaperMarioColors.StageRed else PaperMarioColors.BorderBrown.copy(alpha = 0.3f)
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .size(12.dp)
                                .background(color, CircleShape)
                        )
                    }
                }
            }
        } // Closes Open Book Layout Box
        
        // Top Right Action Buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            androidx.compose.material3.IconButton(
                onClick = { com.example.utils.SoundManager.toggleBgm() },
                modifier = Modifier
                    .background(PaperMarioColors.SkyBlue, CircleShape)
                    .size(40.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = if (com.example.utils.SoundManager.isBgmEnabled.value) 
                        Icons.Filled.Notifications 
                    else 
                        Icons.Filled.Close,
                    contentDescription = "Toggle BGM",
                    tint = Color.White
                )
            }
            androidx.compose.material3.IconButton(
                onClick = { showResetWarning = true },
                modifier = Modifier
                    .background(Color(0xFFE67E22), CircleShape)
                    .size(40.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset Progress",
                    tint = Color.White
                )
            }
        }
    } // Closes Outer Box
} // Closes HomeScreen

@Composable

fun DioramaPage(chapter: StoryChapter, onPlayClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "diorama_animation")
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        PaperBanner(
            text = chapter.title.uppercase(),
            backgroundColor = PaperMarioColors.StageRed,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2.5D Diorama Scene
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer {
                    rotationX = 15f
                    cameraDistance = 8 * density
                },
            contentAlignment = Alignment.Center
        ) {
            // Background Sky / Scene
            CardboardContainer(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = PaperMarioColors.SkyBlue,
                hasStitches = false
            ) {
                // Clouds
                Text(
                    "☁️",
                    fontSize = 60.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 20.dp + cloudOffset.dp, y = 20.dp)
                        .graphicsLayer { shadowElevation = 10f }
                )
                Text(
                    "☁️",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = (-30).dp - cloudOffset.dp, y = 40.dp)
                        .graphicsLayer { shadowElevation = 15f }
                )

                // Ground
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(PaperMarioColors.GrassGreen)
                )

                // Decor (Trees, Houses)
                Text(
                    "🌲",
                    fontSize = 70.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 30.dp, y = (-20).dp)
                        .graphicsLayer { shadowElevation = 20f }
                )
                
                Text(
                    "🏠",
                    fontSize = 80.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-20).dp, y = (-10).dp)
                        .graphicsLayer { shadowElevation = 25f }
                )

                // Main Character
                StickerCharacter(
                    emoji = if (chapter.unlocked) chapter.emoji else "🔒",
                    size = 130.dp,
                    hasPedestal = true,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 20.dp)
                        .graphicsLayer { 
                            shadowElevation = 40f 
                        }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        if (chapter.unlocked) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) { idx ->
                    Text(
                        text = if (idx < chapter.starsEarned) "⭐" else "☆",
                        fontSize = 24.sp,
                        color = if (idx < chapter.starsEarned) Color(0xFFFFC107) else Color(0x55000000)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ChunkyGameButton(
                onClick = onPlayClick,
                containerColor = PaperMarioColors.StageRed,
                bevelColor = Color(0xFF7F0000)
            ) {
                Text("JUGAR", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        } else {
            CardboardContainer(backgroundColor = Color(0xFFEBE5DB)) {
                Text(
                    "Bloqueado\n¡Supera el nivel anterior!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

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
            .clickable(enabled = enabled, onClick = {
                com.example.utils.SoundManager.playPop()
                onClick()
            })
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
fun CreditsTriggerPage(allCompleted: Boolean, onTriggerClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaperBanner(
            text = "CRÉDITOS",
            backgroundColor = PaperMarioColors.StageRed,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (allCompleted) {
                CardboardContainer(backgroundColor = PaperMarioColors.SkyBlue) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "¡Felicidades!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Has completado todos los cuentos.",
                            fontSize = 16.sp,
                            color = PaperMarioColors.BorderBrown,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ChunkyGameButton(
                            onClick = onTriggerClick,
                            containerColor = PaperMarioColors.StageRed,
                            bevelColor = Color(0xFF7F0000)
                        ) {
                            Text("VER CRÉDITOS", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            } else {
                CardboardContainer(backgroundColor = Color(0xFFEBE5DB)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "BLOQUEADO",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Termina los 10 cuentos\npara ver los créditos.",
                            fontSize = 16.sp,
                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
