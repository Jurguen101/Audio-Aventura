import re

with open("app/src/main/java/com/example/ui/screens/CreditsScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    'text = "   Juan Carlos',
    'modifier = Modifier.padding(start = 32.dp),\n                        text = "Juan Carlos'
)
content = content.replace(
    'text = "   Jurguen',
    'modifier = Modifier.padding(start = 32.dp),\n                        text = "Jurguen'
)

with open("app/src/main/java/com/example/ui/screens/CreditsScreen.kt", "w") as f:
    f.write(content)
