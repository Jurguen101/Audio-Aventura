import re

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "r") as f:
    content = f.read()

content = content.replace("quadraticBezierTo", "quadraticTo")

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "w") as f:
    f.write(content)
