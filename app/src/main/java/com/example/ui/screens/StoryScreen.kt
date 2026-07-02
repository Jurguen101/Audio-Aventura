package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.*

@Composable
fun StoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeChapterId by viewModel.activeChapterId.collectAsState()
    val chapters by viewModel.allChapters.collectAsState()
    val currentPageIndex by viewModel.currentPageIndex.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val currentChapter = chapters.find { it.id == activeChapterId }
    val pages = viewModel.getActivePages()
    val currentPage = pages.getOrNull(currentPageIndex)

    if (currentChapter == null || currentPage == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateTo(Screen.Home)
        }
        return
    }

    // Audio breathing scale animation
    val infiniteTransition = rememberInfiniteTransition(label = "StoryAudioPulse")
    val pulseScale by if (isSpeaking) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "StorySpeakPulse"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5E6CC)) // Cozy craft paper background
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left Column: The Interactive Diorama Stage (fills 55% of screen width)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.3f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Upper Header Navigation Row inside left pane
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardboardButton(
                    onClick = { viewModel.navigateTo(Screen.Home) },
                    containerColor = PaperMarioColors.PaperWhite
                ) {
                    Text(
                        text = "🏠 INICIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown
                    )
                }

                PaperBanner(
                    text = "${currentChapter.title} ${currentChapter.emoji}",
                    backgroundColor = PaperMarioColors.BannerYellow,
                    textColor = PaperMarioColors.BorderBrown
                )
            }

            // 1. DIORAMA POP-UP PARALLAX CANVAS with custom pop-up unfolding animation
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    val isForward = targetState.pageNumber > initialState.pageNumber
                    if (isForward) {
                        // Unfold pop-up from bottom edge
                        (slideInVertically(animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)) { it } +
                                scaleIn(
                                    initialScale = 0.1f,
                                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f),
                                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)
                                ) +
                                fadeIn(animationSpec = tween(350)))
                            .togetherWith(
                                slideOutVertically(animationSpec = tween(250)) { -it / 2 } +
                                        scaleOut(
                                            targetScale = 0.8f,
                                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f),
                                            animationSpec = tween(250)
                                        ) +
                                        fadeOut(animationSpec = tween(250))
                            )
                    } else {
                        // Fold back/down transition
                        (slideInVertically(animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)) { -it } +
                                scaleIn(
                                    initialScale = 0.1f,
                                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f),
                                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow)
                                ) +
                                fadeIn(animationSpec = tween(350)))
                            .togetherWith(
                                slideOutVertically(animationSpec = tween(250)) { it / 2 } +
                                        scaleOut(
                                            targetScale = 0.8f,
                                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f),
                                            animationSpec = tween(250)
                                        ) +
                                        fadeOut(animationSpec = tween(250))
                            )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = "StoryDioramaUnfold"
            ) { targetPage ->
                PopUpDioramaCanvas(
                    mainEmoji = targetPage.mainDioramaEmoji,
                    floatingEmojis = targetPage.floatingDioramaEmojis,
                    backgroundColors = targetPage.backgroundGradientColors,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Right Column: Narration, text, and page actions (fills remaining width)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Page fraction badge & indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(PaperMarioColors.StageRed, RoundedCornerShape(4.dp))
                        .border(1.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "📖 NARRACIÓN DE AVENTURAS",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .background(PaperMarioColors.PaperWhite, shape = RoundedCornerShape(10.dp))
                        .border(1.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${currentPageIndex + 1}/${pages.size}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown
                    )
                }
            }

            // 2. STORY BOOK TEXT COVER WITH SMOOTH PAGE FLIP EFFECT
            CardboardContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                backgroundColor = PaperMarioColors.PaperWhite,
                cornerRadius = 12.dp,
                hasStitches = false
            ) {
                AnimatedContent(
                    targetState = currentPage,
                    transitionSpec = {
                        val isForward = targetState.pageNumber > initialState.pageNumber
                        if (isForward) {
                            (slideInHorizontally(animationSpec = tween(350)) { it } + fadeIn(animationSpec = tween(350)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(250)) { -it } + fadeOut(animationSpec = tween(250)))
                        } else {
                            (slideInHorizontally(animationSpec = tween(350)) { -it } + fadeIn(animationSpec = tween(350)))
                                .togetherWith(slideOutHorizontally(animationSpec = tween(250)) { it } + fadeOut(animationSpec = tween(250)))
                        }
                    },
                    label = "StoryTextPageFlip"
                ) { targetPage ->
                    Text(
                        text = targetPage.textContent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PaperMarioColors.BorderBrown,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. STITCHED CARDBOARD AUDIO CONTROL
            CardboardContainer(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isSpeaking) Color(0xFFE8F8F5) else PaperMarioColors.PaperWhite,
                borderColor = if (isSpeaking) PaperMarioColors.GrassGreen else PaperMarioColors.BorderBrown,
                cornerRadius = 10.dp,
                hasStitches = true
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            com.example.utils.SoundManager.playPop()
                            if (isSpeaking) {
                                viewModel.stopSpeaking()
                            } else {
                                viewModel.speakCurrentPage()
                            }
                        }
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .scale(pulseScale)
                            .background(
                                if (isSpeaking) PaperMarioColors.GrassGreen else PaperMarioColors.BorderBrown,
                                shape = CircleShape
                            )
                            .border(1.5.dp, Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSpeaking) "⏸️" else "🔊",
                            fontSize = 18.sp
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isSpeaking) "Narrador leyendo..." else "Escuchar narración de voz",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = PaperMarioColors.BorderBrown
                        )
                    }

                    if (isSpeaking) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(3) { idx ->
                                val dotAnim = infiniteTransition.animateFloat(
                                    initialValue = -3f,
                                    targetValue = 3f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(400, delayMillis = idx * 100, easing = EaseInOutSine),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "DotAnim"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .offset(y = dotAnim.value.dp)
                                        .background(PaperMarioColors.GrassGreen, shape = CircleShape)
                                )
                            }
                        }
                    }
                }
            }

            // 3.5. COLLAPSIBLE VOICE SETTINGS
            var showVoiceSettings by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { 
                            com.example.utils.SoundManager.playPop()
                            showVoiceSettings = !showVoiceSettings 
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showVoiceSettings) "▲ Ocultar ajustes de voz ⚙️" else "▼ Configurar voz del narrador ⚙️",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PaperMarioColors.BorderBrown.copy(alpha = 0.75f)
                    )
                }

                if (showVoiceSettings) {
                    val currentSpeed by viewModel.narratorSpeed.collectAsState()
                    val currentPitch by viewModel.narratorPitch.collectAsState()

                    CardboardContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 4.dp),
                        backgroundColor = Color(0xFFFFFDF2),
                        borderColor = PaperMarioColors.BorderBrown.copy(alpha = 0.4f),
                        cornerRadius = 10.dp,
                        hasStitches = true
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // SPEED CONTROL PRESETS
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Velocidad de Lectura (Cadencia)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val speeds = listOf(
                                        Triple("Lento (🐢)", 0.85f, Color(0xFFFFF9C4)),
                                        Triple("Normal (⚡)", 1.0f, Color(0xFFE8F8F5)),
                                        Triple("Gemini Live (🎙️)", 1.05f, Color(0xFFEBF5FB))
                                    )
                                    speeds.forEach { (label, speedVal, selectedBg) ->
                                        val isSelected = kotlin.math.abs(currentSpeed - speedVal) < 0.01f
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { 
                                                    com.example.utils.SoundManager.playPop()
                                                    viewModel.setNarratorSpeed(speedVal) 
                                                }
                                                .background(
                                                    if (isSelected) selectedBg else Color.Transparent,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    if (isSelected) 2.dp else 1.dp,
                                                    if (isSelected) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                color = if (isSelected) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            }

                            // PITCH CONTROL PRESETS
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = "Tono de la Voz (Frecuencia)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PaperMarioColors.BorderBrown
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val pitches = listOf(
                                        Triple("Natural (🧑)", 1.0f, Color(0xFFFDEDEC)),
                                        Triple("Gemini Live (💬)", 1.05f, Color(0xFFE8F8F5)),
                                        Triple("Cálido (🐥)", 1.15f, Color(0xFFFEF9E7))
                                    )
                                    pitches.forEach { (label, pitchVal, selectedBg) ->
                                        val isSelected = kotlin.math.abs(currentPitch - pitchVal) < 0.01f
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { 
                                                    com.example.utils.SoundManager.playPop()
                                                    viewModel.setNarratorPitch(pitchVal) 
                                                }
                                                .background(
                                                    if (isSelected) selectedBg else Color.Transparent,
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    if (isSelected) 2.dp else 1.dp,
                                                    if (isSelected) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.25f),
                                                    RoundedCornerShape(8.dp)
                                                )
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                                color = if (isSelected) PaperMarioColors.BorderBrown else PaperMarioColors.BorderBrown.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4. LOWER NAVIGATION ACTION BUTTONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (currentPageIndex > 0) {
                    CardboardButton(
                        onClick = { viewModel.prevPage() },
                        containerColor = PaperMarioColors.PaperWhite,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "◀ ANT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = PaperMarioColors.BorderBrown
                        )
                    }
                }

                val isLastPage = currentPageIndex == pages.size - 1
                val nextButtonBg = if (isLastPage) PaperMarioColors.BannerYellow else PaperMarioColors.SkyBlue
                val nextButtonText = if (isLastPage) "TRIVIA 🎓✨" else "SIGUIENTE ▶"

                CardboardButton(
                    onClick = {
                        if (isLastPage) {
                            viewModel.navigateTo(Screen.Trivia)
                        } else {
                            viewModel.nextPage()
                        }
                    },
                    containerColor = nextButtonBg,
                    modifier = Modifier.weight(1.3f)
                ) {
                    Text(
                        text = nextButtonText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown
                    )
                }
            }
        }
    }
}
