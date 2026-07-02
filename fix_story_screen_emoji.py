import re

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "r") as f:
    content = f.read()

replacement = """
                val mainCharEmoji = when (currentChapter?.id) {
                    "ninja" -> "🥷"
                    "fantasma" -> "👻"
                    "vaquero" -> "🤠"
                    "rey" -> "👑"
                    "robot" -> "🤖"
                    "dragon" -> "🐉"
                    "pirata" -> "🏴\u200d☠️"
                    "astronauta" -> "🧑\u200d🚀"
                    "ardilla" -> "🐿️"
                    "mago" -> "🧙\u200d♂️"
                    else -> targetPage.mainDioramaEmoji
                }

                PopUpDioramaCanvas(
                    mainEmoji = mainCharEmoji,
"""

content = content.replace(
"""                PopUpDioramaCanvas(
                    mainEmoji = targetPage.mainDioramaEmoji,""", replacement
)

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "w") as f:
    f.write(content)
