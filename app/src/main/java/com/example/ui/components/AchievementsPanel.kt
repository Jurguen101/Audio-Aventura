package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CollectibleCharacter
import com.example.data.StoryChapter
import com.example.data.UserProgress

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: (UserProgress, List<StoryChapter>, List<CollectibleCharacter>) -> Boolean
)

val GameAchievements = listOf(
    Achievement(
        id = "first_star",
        title = "Estrella Naciente",
        description = "Gana tu primera estrella.",
        emoji = "🌟",
        isUnlocked = { progress, _, _ -> progress.starsCount >= 1 }
    ),
    Achievement(
        id = "collector",
        title = "Coleccionista",
        description = "Desbloquea 2 personajes.",
        emoji = "🎒",
        isUnlocked = { _, _, chars -> chars.count { it.unlocked } >= 2 }
    ),
    Achievement(
        id = "reader",
        title = "Gran Lector",
        description = "Completa 2 cuentos.",
        emoji = "📚",
        isUnlocked = { _, chapters, _ -> chapters.count { it.completed } >= 2 }
    ),
    Achievement(
        id = "master",
        title = "Maestro de Cartón",
        description = "Gana 10 estrellas.",
        emoji = "👑",
        isUnlocked = { progress, _, _ -> progress.starsCount >= 10 }
    )
)

@Composable
fun AchievementsPanel(
    progress: UserProgress,
    chapters: List<StoryChapter>,
    characters: List<CollectibleCharacter>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "LOGROS 🏆",
            fontWeight = FontWeight.Black,
            fontSize = 16.sp,
            color = PaperMarioColors.BorderBrown,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(GameAchievements) { achievement ->
                val unlocked = achievement.isUnlocked(progress, chapters, characters)
                AchievementMedal(
                    achievement = achievement,
                    unlocked = unlocked
                )
            }
        }
    }
}

@Composable
fun AchievementMedal(
    achievement: Achievement,
    unlocked: Boolean
) {
    CardboardContainer(
        backgroundColor = if (unlocked) Color(0xFFFFD700) else Color(0xFFE0E0E0),
        borderColor = if (unlocked) Color(0xFFB8860B) else Color(0xFFA0A0A0),
        cornerRadius = 12.dp,
        hasStitches = unlocked,
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .alpha(if (unlocked) 1f else 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                CharacterGraphic(
                    emoji = if (unlocked) achievement.emoji else "🔒",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = achievement.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = PaperMarioColors.BorderBrown,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
    }
}
