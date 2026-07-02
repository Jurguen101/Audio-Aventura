import re

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
"""                PopUpDioramaCanvas(
                    mainEmoji = targetPage.mainDioramaEmoji,
                    floatingEmojis = targetPage.floatingDioramaEmojis,
                    backgroundColors = targetPage.backgroundGradientColors,""",
"""                PopUpDioramaCanvas(
                    mainEmoji = targetPage.mainDioramaEmoji,
                    floatingEmojis = targetPage.floatingDioramaEmojis,
                    storyTheme = currentChapter?.id ?: "",
                    backgroundColors = targetPage.backgroundGradientColors,"""
)

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "w") as f:
    f.write(content)
