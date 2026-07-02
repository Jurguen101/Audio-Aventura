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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
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
fun TriviaScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeChapterId by viewModel.activeChapterId.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val starsEarned by viewModel.starsEarnedInCurrentAdventure.collectAsState()
    val selectedOption by viewModel.selectedTriviaOption.collectAsState()
    val isAnswered by viewModel.isTriviaAnswered.collectAsState()
    val isCorrect by viewModel.isTriviaCorrect.collectAsState()
    val progress by viewModel.userProgress.collectAsState()

    val wildcardsCount = progress?.wildcardsCount ?: 0
    val questions = viewModel.getActiveQuestions()
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (currentQuestion == null) {
        LaunchedEffect(Unit) {
            viewModel.navigateTo(Screen.Home)
        }
        return
    }

    // Speak question out loud when index updates
    LaunchedEffect(currentQuestionIndex) {
        viewModel.speakText("Pregunta número ${currentQuestionIndex + 1}. De dificultad: ${currentQuestion.difficultyLabel}. ${currentQuestion.questionText}")
    }

    val infiniteTransition = rememberInfiniteTransition(label = "TriviaStarBreathing")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TriviaStarPulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5E6CC)) // Craft Cardboard backdrop
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Red Theater ribbon decoration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(PaperMarioColors.StageRed, RoundedCornerShape(4.dp))
                .border(1.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🎓 DESAFÍO DE PREGUNTAS DE CARTÓN 🎓",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Upper Header row
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
                    text = "🏠 SALIR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = PaperMarioColors.BorderBrown
                )
            }

            PaperBanner(
                text = "Dificultad: ${currentQuestion.difficultyLabel}",
                backgroundColor = when (currentQuestion.difficultyLabel) {
                    "Fácil ⭐" -> PaperMarioColors.GrassGreen
                    "Medio-Fácil ⭐" -> PaperMarioColors.SkyBlue
                    "Medio ⭐" -> PaperMarioColors.BannerYellow
                    "Difícil ⭐" -> Color(0xFFE67E22)
                    else -> PaperMarioColors.StageRed
                },
                textColor = if (currentQuestion.difficultyLabel == "Fácil ⭐" || currentQuestion.difficultyLabel == "Muy Difícil ⭐") Color.White else PaperMarioColors.BorderBrown
            )

            // Dynamic progress count
            Box(
                modifier = Modifier
                    .background(PaperMarioColors.PaperWhite, shape = RoundedCornerShape(10.dp))
                    .border(1.5.dp, PaperMarioColors.BorderBrown, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${currentQuestionIndex + 1}/${questions.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = PaperMarioColors.BorderBrown
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // PROGRESS STARS DISPLAY BAR (Visually shows stars earned during this run)
        CardboardContainer(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = PaperMarioColors.PaperWhite,
            cornerRadius = 10.dp,
            hasStitches = false
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estrellas en este cuento:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PaperMarioColors.BorderBrown
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(questions.size) { index ->
                        val isStarEarned = index < starsEarned
                        Text(
                            text = if (isStarEarned) "⭐" else "⚫",
                            fontSize = 18.sp,
                            modifier = if (isStarEarned) Modifier.scale(starScale) else Modifier
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. TRIVIA QUESTION TEXT CARD
        CardboardContainer(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFFCF3CF), // Golden warm paper cutout
            borderColor = PaperMarioColors.BorderBrown,
            cornerRadius = 14.dp,
            hasStitches = true
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Pregunta ${currentQuestionIndex + 1}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFD35400)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = currentQuestion.questionText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PaperMarioColors.BorderBrown,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. CHOICES OPTIONS GRID (OR VERTICAL LIST)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            currentQuestion.options.forEach { option ->
                val isSelected = selectedOption == option
                val optionBg = if (isSelected) Color(0xFFE8F8F5) else PaperMarioColors.PaperWhite
                val optionBorder = if (isSelected) PaperMarioColors.GrassGreen else PaperMarioColors.BorderBrown

                CardboardContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isAnswered) { viewModel.selectTriviaOption(option) },
                    backgroundColor = optionBg,
                    borderColor = optionBorder,
                    cornerRadius = 12.dp,
                    hasStitches = false
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Option circular bullet
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    if (isSelected) PaperMarioColors.GrassGreen else Color(0xFFF0F3F4),
                                    shape = CircleShape
                                )
                                .border(1.5.dp, PaperMarioColors.BorderBrown, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isSelected) "✓" else option.take(2).trim(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else PaperMarioColors.BorderBrown.copy(alpha = 0.6f)
                            )
                        }

                        Text(
                            text = option.substring(3),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. ANSWER VERIFICATION OR CONSEQUENCE AREA
        if (!isAnswered) {
            CardboardButton(
                onClick = { viewModel.checkTriviaAnswer() },
                containerColor = if (selectedOption != null) PaperMarioColors.GrassGreen else Color(0xFFBDC3C7),
                enabled = selectedOption != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¡VERIFICAR RESPUESTA! 🚀",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        } else {
            // An response has been given!
            if (isCorrect) {
                // Correct answer block
                CardboardContainer(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFE8F8F5), // Golden/Green success
                    borderColor = PaperMarioColors.GrassGreen,
                    cornerRadius = 12.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🎉 ¡EXCELENTE! 🎉",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1E8449)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¡Has respondido de manera perfecta y ganaste una estrella de papel!",
                            fontSize = 12.sp,
                            color = Color(0xFF27AE60),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                val isLastQuestion = currentQuestionIndex == questions.size - 1
                CardboardButton(
                    onClick = { viewModel.nextQuestion() },
                    containerColor = PaperMarioColors.SkyBlue,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isLastQuestion) "FINALIZAR AVENTURA 🎓🏆" else "SIGUIENTE PREGUNTA ▶",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = PaperMarioColors.BorderBrown
                    )
                }
            } else {
                // Incorrect answer block (The user has the chance to use wildcard!)
                CardboardContainer(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFFDEDEC), // Pastel warning red
                    borderColor = PaperMarioColors.StageRed,
                    cornerRadius = 12.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "❌ ¡OH, CASI LO LOGRAS! ❌",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFC0392B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Esa opción no es la correcta. ¿Quieres usar una Carta Comodín para volver a intentarlo?",
                            fontSize = 12.sp,
                            color = Color(0xFFC0392B).copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Option 1: Use Wildcard to retry this specific question
                    CardboardButton(
                        onClick = { viewModel.useWildcardToRetryQuestion() },
                        containerColor = PaperMarioColors.BannerYellow,
                        enabled = wildcardsCount >= 1,
                        modifier = Modifier.weight(1.2f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🃏 USAR COMODÍN",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = PaperMarioColors.BorderBrown
                            )
                            Text(
                                text = "(Tienes: $wildcardsCount)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PaperMarioColors.BorderBrown.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Option 2: Skip / Continue without gaining this question's star
                    val isLastQuestion = currentQuestionIndex == questions.size - 1
                    CardboardButton(
                        onClick = { viewModel.nextQuestion() },
                        containerColor = PaperMarioColors.PaperWhite,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = if (isLastQuestion) "CONTINUAR 🏁" else "CONTINUAR ▶",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = PaperMarioColors.BorderBrown
                        )
                    }
                }
            }
        }
    }
}
