package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val id: Int = 1,
    val starsCount: Int = 0,
    val wildcardsCount: Int = 0
)

@Entity(tableName = "story_chapters")
data class StoryChapter(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val unlocked: Boolean = false,
    val completed: Boolean = false,
    val starsEarned: Int = 0,
    val maxStars: Int = 5, // Now 5 questions, 5 max stars
    val orderIndex: Int = 0
)

@Entity(tableName = "collectible_characters")
data class CollectibleCharacter(
    @PrimaryKey val id: String,
    val name: String,
    val chapterId: String,
    val emoji: String,
    val description: String,
    val soundPhrase: String,
    val unlocked: Boolean = false,
    val priceStars: Int = 3 // Standard price of 3 stars
)

// In-Memory Story Page definition
data class StoryPage(
    val pageNumber: Int,
    val textContent: String,
    val backgroundGradientColors: List<Long>,
    val mainDioramaEmoji: String,
    val floatingDioramaEmojis: List<String>
)

