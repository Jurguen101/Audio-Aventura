import re

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "r") as f:
    content = f.read()

# Change getDioramaTheme signature and implementation
content = content.replace("fun getDioramaTheme(mainEmoji: String): DioramaThemeColors {", "fun getDioramaTheme(storyTheme: String): DioramaThemeColors {")

content = content.replace('when (mainEmoji) {', 'when (storyTheme) {')

theme_mapping = [
    ('🥷', 'ninja'),
    ('👻', 'fantasma'),
    ('🤠', 'vaquero'),
    ('👑', 'rey'),
    ('🤖', 'robot'),
    ('🐉', 'dragon'),
    ('🏴\u200d☠️', 'pirata'),
    ('🧑\u200d🚀', 'astronauta'),
    ('🐿️', 'ardilla'),
    ('🧙\u200d♂️', 'mago'),
]
for old, new in theme_mapping:
    content = content.replace(f'"{old}" -> DioramaThemeColors', f'"{new}" -> DioramaThemeColors')

# Add storyTheme to PopUpDioramaCanvas
content = content.replace(
    "fun PopUpDioramaCanvas(\n    mainEmoji: String,\n    floatingEmojis: List<String>,",
    "fun PopUpDioramaCanvas(\n    mainEmoji: String,\n    floatingEmojis: List<String>,\n    storyTheme: String = \"\",\n"
)

# Use storyTheme instead of mainEmoji for the colors
content = content.replace("getDioramaTheme(mainEmoji)", "getDioramaTheme(storyTheme)")

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "w") as f:
    f.write(content)
