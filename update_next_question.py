import re

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "r") as f:
    content = f.read()

old_code = """    fun nextQuestion() {
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
    }"""

new_code = """    fun nextQuestion() {
        val questions = getActiveQuestions()
        if (_currentQuestionIndex.value < questions.size - 1) {
            _currentQuestionIndex.value += 1
            _selectedTriviaOption.value = null
            _isTriviaAnswered.value = false
            _isTriviaCorrect.value = false
        } else {
            val chapterId = _activeChapterId.value ?: return
            val starsEarned = _starsEarnedInCurrentAdventure.value
            val previouslyCompletedCount = _allChapters.value.count { it.completed }
            val chapterWasNotCompleted = _allChapters.value.find { it.id == chapterId }?.completed == false
            
            viewModelScope.launch {
                repository.addStars(starsEarned)
                repository.completeChapter(chapterId, starsEarned)
            }
            
            if (chapterWasNotCompleted && previouslyCompletedCount == 9) {
                navigateTo(Screen.Credits)
            } else {
                navigateTo(Screen.Home)
            }
        }
    }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/ui/MainViewModel.kt", "w") as f:
        f.write(content)
    print("Updated nextQuestion.")
else:
    print("Failed to find nextQuestion.")
