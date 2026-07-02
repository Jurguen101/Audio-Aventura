import re

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "r") as f:
    content = f.read()

# Add FinalCredits to enum
old_enum = """enum class Screen {
    Credits,
    Home,
    StoryPreview,
    Story,
    Trivia,
    Characters
}"""
new_enum = """enum class Screen {
    Credits,
    FinalCredits,
    Home,
    StoryPreview,
    Story,
    Trivia,
    Characters
}"""
if old_enum in content:
    content = content.replace(old_enum, new_enum)
    print("Updated Screen enum.")
else:
    print("Could not find Screen enum.")

# Change initial screen
old_initial = """    private val _currentScreen = MutableStateFlow(Screen.Home)"""
new_initial = """    private val _currentScreen = MutableStateFlow(Screen.Credits)"""
if old_initial in content:
    content = content.replace(old_initial, new_initial)
    print("Updated initial screen.")
else:
    print("Could not find initial screen.")

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "w") as f:
    f.write(content)
