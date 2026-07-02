package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AdventureDao {

    // User Progress
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getUserProgressFlow(): Flow<UserProgress?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getUserProgress(): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProgress(progress: UserProgress)

    // Story Chapters
    @Query("SELECT * FROM story_chapters ORDER BY orderIndex ASC")
    fun getAllChaptersFlow(): Flow<List<StoryChapter>>

    @Query("SELECT * FROM story_chapters WHERE id = :chapterId")
    suspend fun getChapter(chapterId: String): StoryChapter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<StoryChapter>)

    @Update
    suspend fun updateChapter(chapter: StoryChapter)

    // Collectible Characters
    @Query("SELECT * FROM collectible_characters ORDER BY name ASC")
    fun getAllCharactersFlow(): Flow<List<CollectibleCharacter>>

    @Query("SELECT * FROM collectible_characters WHERE chapterId = :chapterId")
    suspend fun getCharacterByChapter(chapterId: String): CollectibleCharacter?

    @Query("SELECT * FROM collectible_characters WHERE id = :id")
    suspend fun getCharacterById(id: String): CollectibleCharacter?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CollectibleCharacter>)

    @Update
    suspend fun updateCharacter(character: CollectibleCharacter)
}
