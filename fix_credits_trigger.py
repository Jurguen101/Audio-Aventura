import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace('"🎉 ¡Felicidades! 🎉"', '"¡Felicidades!"')
content = content.replace('"🔒 Bloqueado"', '"BLOQUEADO"')

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
