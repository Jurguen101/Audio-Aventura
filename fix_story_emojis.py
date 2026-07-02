import re

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "r") as f:
    content = f.read()

content = content.replace('"TRIVIA 🎓✨"', '"TRIVIA"')
content = content.replace('"SIGUIENTE ▶"', '"SIGUIENTE"')

with open("app/src/main/java/com/example/ui/screens/StoryScreen.kt", "w") as f:
    f.write(content)
