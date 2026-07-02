package com.example.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

enum class Screen {
    Credits,
    Home,
    StoryPreview,
    Story,
    Trivia,
    Characters
}

class MainViewModel(
    application: Application,
    private val repository: StoryRepository
) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    // Database Flows
    val userProgress: StateFlow<UserProgress?> = repository.userProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allChapters: StateFlow<List<StoryChapter>> = repository.allChapters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCharacters: StateFlow<List<CollectibleCharacter>> = repository.allCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Screen navigation
    private val _currentScreen = MutableStateFlow(Screen.Credits)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        if (screen == Screen.Home) {
            stopSpeaking()
        }
    }

    // Active Story & Navigation State
    private val _activeChapterId = MutableStateFlow<String?>(null)
    val activeChapterId: StateFlow<String?> = _activeChapterId.asStateFlow()

    private val _activeTriviaQuestions = MutableStateFlow<List<TriviaQuestion>>(emptyList())
    val activeTriviaQuestions: StateFlow<List<TriviaQuestion>> = _activeTriviaQuestions.asStateFlow()

    private val _currentPageIndex = MutableStateFlow(0)
    val currentPageIndex: StateFlow<Int> = _currentPageIndex.asStateFlow()

    // Trivia State
    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _starsEarnedInCurrentAdventure = MutableStateFlow(0)
    val starsEarnedInCurrentAdventure: StateFlow<Int> = _starsEarnedInCurrentAdventure.asStateFlow()

    private val _selectedTriviaOption = MutableStateFlow<String?>(null)
    val selectedTriviaOption: StateFlow<String?> = _selectedTriviaOption.asStateFlow()

    private val _isTriviaAnswered = MutableStateFlow(false)
    val isTriviaAnswered: StateFlow<Boolean> = _isTriviaAnswered.asStateFlow()

    private val _isTriviaCorrect = MutableStateFlow(false)
    val isTriviaCorrect: StateFlow<Boolean> = _isTriviaCorrect.asStateFlow()

    // Text To Speech State
    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _narratorSpeed = MutableStateFlow(1.05f)
    val narratorSpeed: StateFlow<Float> = _narratorSpeed.asStateFlow()

    private val _narratorPitch = MutableStateFlow(1.05f)
    val narratorPitch: StateFlow<Float> = _narratorPitch.asStateFlow()

    fun setNarratorSpeed(speed: Float) {
        _narratorSpeed.value = speed
        tts?.setSpeechRate(speed)
    }

    fun setNarratorPitch(pitch: Float) {
        _narratorPitch.value = pitch
        tts?.setPitch(pitch)
    }

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
        try {
            val ctx = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                application.createAttributionContext("my_tag")
            } else {
                application
            }
            tts = TextToSpeech(ctx, this)
        } catch (e: Exception) {
            Log.e("MainViewModel", "Failed to initialize TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setAudioAttributes(
                android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_GAME)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            )
            var voiceSelected = false
            try {
                val voices = tts?.voices
                if (voices != null && voices.isNotEmpty()) {
                    val spanishVoices = voices.filter { voice ->
                        voice.locale.language.startsWith("es", ignoreCase = true) &&
                        !voice.features.contains("notInstalled")
                    }
                    if (spanishVoices.isNotEmpty()) {
                        // Prioritize network/neural/wavenet voices for natural, fluid conversation (Gemini Live style)
                        val bestVoice = spanishVoices.sortedWith(compareByDescending<Voice> {
                            it.name.contains("network", ignoreCase = true)
                        }.thenByDescending {
                            it.name.contains("neural", ignoreCase = true) || it.name.contains("wavenet", ignoreCase = true)
                        }.thenByDescending {
                            it.quality
                        }.thenBy {
                            if (it.locale.country.equals("ES", ignoreCase = true)) 2 else if (it.locale.country.equals("US", ignoreCase = true)) 1 else 0
                        }).firstOrNull()

                        if (bestVoice != null) {
                            tts?.voice = bestVoice
                            voiceSelected = true
                            Log.d("MainViewModel", "Selected Gemini-style Spanish Voice: ${bestVoice.name} (Quality: ${bestVoice.quality})")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error selecting custom voice, falling back", e)
            }

            if (!voiceSelected) {
                val result = tts?.setLanguage(Locale.forLanguageTag("es-ES")) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    val fallbackResult = tts?.setLanguage(Locale.forLanguageTag("es")) ?: TextToSpeech.LANG_NOT_SUPPORTED
                    if (fallbackResult == TextToSpeech.LANG_MISSING_DATA || fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.language = Locale.getDefault()
                    }
                }
            }

            // Set initial dynamic storytelling parameters
            tts?.setSpeechRate(_narratorSpeed.value)
            tts?.setPitch(_narratorPitch.value)

            _isTtsReady.value = true
            tts?.setOnUtteranceProgressListener(object : android.speech.tts.UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    _isSpeaking.value = false
                }
            })
        } else {
            _isTtsReady.value = false
        }
    }

    // Story Management actions
    fun selectPreviewChapter(chapterId: String) {
        _activeChapterId.value = chapterId
    }

    fun startChapter(chapterId: String) {
        _activeChapterId.value = chapterId
        _currentPageIndex.value = 0
        _currentQuestionIndex.value = 0
        _starsEarnedInCurrentAdventure.value = 0
        
        // Load 5 random questions for this chapter and randomize options
        val allQuestions = TriviaProvider.getQuestionsForChapter(chapterId)
        _activeTriviaQuestions.value = allQuestions.shuffled().take(5).map { q ->
            q.copy(options = q.options.shuffled())
        }
        
        resetTriviaState()
        stopSpeaking()
        speakCurrentPage()
    }

    fun nextPage() {
        val chapterId = _activeChapterId.value ?: return
        val pages = repository.getPagesForChapter(chapterId)
        if (_currentPageIndex.value < pages.size - 1) {
            _currentPageIndex.value += 1
            stopSpeaking()
            speakCurrentPage()
        }
    }

    fun prevPage() {
        if (_currentPageIndex.value > 0) {
            _currentPageIndex.value -= 1
            stopSpeaking()
            speakCurrentPage()
        }
    }

    fun getActivePages(): List<StoryPage> {
        val chapterId = _activeChapterId.value ?: return emptyList()
        return repository.getPagesForChapter(chapterId)
    }

    // TTS Control actions
    fun speakCurrentPage() {
        val chapterId = _activeChapterId.value ?: return
        val pages = repository.getPagesForChapter(chapterId)
        val index = _currentPageIndex.value
        if (index in pages.indices) {
            speakText(pages[index].textContent)
        }
    }

    fun speakText(text: String) {
        if (_isTtsReady.value && tts != null) {
            stopSpeaking()
            _isSpeaking.value = true
            tts?.setSpeechRate(_narratorSpeed.value)
            tts?.setPitch(_narratorPitch.value)
            val params = android.os.Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "page_speech")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "page_speech")
        }
    }

    fun stopSpeaking() {
        if (tts != null && tts?.isSpeaking == true) {
            tts?.stop()
        }
        _isSpeaking.value = false
    }

    // Trivia actions
    fun selectTriviaOption(option: String) {
        if (!_isTriviaAnswered.value) {
            _selectedTriviaOption.value = option
        }
    }

    fun getActiveQuestions(): List<TriviaQuestion> {
        return _activeTriviaQuestions.value
    }

    fun checkTriviaAnswer() {
        val chapterId = _activeChapterId.value ?: return
        val selected = _selectedTriviaOption.value ?: return
        val questions = getActiveQuestions()
        val currentIndex = _currentQuestionIndex.value
        if (currentIndex !in questions.indices) return

        val question = questions[currentIndex]
        _isTriviaAnswered.value = true

        if (selected == question.correctAnswer) {
            _isTriviaCorrect.value = true
            _starsEarnedInCurrentAdventure.value += 1
            speakText("¡Excelente respuesta! Has ganado una estrella de papel.")
        } else {
            _isTriviaCorrect.value = false
            speakText("¡Oh, no es correcto! Pero puedes usar una carta comodín de papel o continuar.")
        }
    }

    fun nextQuestion() {
        val questions = getActiveQuestions()
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            _selectedTriviaOption.value = null
            _isTriviaAnswered.value = false
            _isTriviaCorrect.value = false
        } else {
            val chapterId = _activeChapterId.value ?: return
            val starsEarned = _starsEarnedInCurrentAdventure.value
            viewModelScope.launch {
                repository.addStars(starsEarned)
                repository.completeChapter(chapterId, starsEarned)
            }
            navigateTo(Screen.Home)
        }
    }

    fun useWildcardToRetryQuestion() {
        viewModelScope.launch {
            val success = repository.useWildcard()
            if (success) {
                _selectedTriviaOption.value = null
                _isTriviaAnswered.value = false
                _isTriviaCorrect.value = false
                speakText("¡Has usado una carta comodín! Intenta responder la pregunta de nuevo.")
            } else {
                speakText("No tienes suficientes cartas comodín de papel.")
            }
        }
    }

    // Shop actions
    fun buyWildcard() {
        viewModelScope.launch {
            val success = repository.buyWildcard()
            if (success) {
                speakText("¡Has comprado una carta comodín de papel por cuatro estrellas!")
            } else {
                speakText("No tienes suficientes estrellas. ¡Resuelve más cuentos de papel!")
            }
        }
    }

    fun buyCharacter(characterId: String) {
        viewModelScope.launch {
            val success = repository.buyCharacter(characterId)
            if (success) {
                speakText("¡Felicidades! Has desbloqueado este personaje de cartón.")
                repository.equipCharacter(characterId) // Auto-equip when bought
            } else {
                speakText("Necesitas tres estrellas para desbloquear este personaje.")
            }
        }
    }

    fun equipCharacter(characterId: String) {
        viewModelScope.launch {
            repository.equipCharacter(characterId)
            speakText("Personaje seleccionado como compañero.")
        }
    }

    fun resetTriviaState() {
        _selectedTriviaOption.value = null
        _isTriviaAnswered.value = false
        _isTriviaCorrect.value = false
    }

    fun resetAllAppProgress() {
        viewModelScope.launch {
            repository.resetProgress()
            _activeChapterId.value = null
            _currentPageIndex.value = 0
            _currentQuestionIndex.value = 0
            _starsEarnedInCurrentAdventure.value = 0
            resetTriviaState()
            stopSpeaking()
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
    }
}
