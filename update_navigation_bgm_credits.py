import re

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "r") as f:
    content = f.read()

old_func = """    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        if (screen == Screen.Home) {
            stopSpeaking()
            com.example.utils.SoundManager.playBgm()
        } else if (screen == Screen.Story) {
            val chapterId = _activeChapterId.value
            if (chapterId != null) {
                com.example.utils.SoundManager.playChapterBgm(chapterId)
            }
        }
    }"""

new_func = """    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
        if (screen == Screen.Home || screen == Screen.Credits || screen == Screen.FinalCredits || screen == Screen.Characters) {
            stopSpeaking()
            com.example.utils.SoundManager.playBgm()
        } else if (screen == Screen.Story) {
            val chapterId = _activeChapterId.value
            if (chapterId != null) {
                com.example.utils.SoundManager.playChapterBgm(chapterId)
            }
        }
    }"""

if old_func in content:
    content = content.replace(old_func, new_func)
    print("Updated navigateTo")
else:
    print("Could not find navigateTo")

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "w") as f:
    f.write(content)
