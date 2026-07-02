import re

def replace_in_file(filepath, replacements):
    with open(filepath, "r") as f:
        content = f.read()
    
    for old, new in replacements:
        content = content.replace(old, new)
        
    with open(filepath, "w") as f:
        f.write(content)

replace_in_file("app/src/main/java/com/example/ui/screens/StoryPreviewScreen.kt", [
    ('"COMENZAR HISTORIA ▶"', '"COMENZAR HISTORIA"'),
    ('"🔒 COMPLETAR ANTERIOR"', '"COMPLETAR ANTERIOR"')
])

replace_in_file("app/src/main/java/com/example/ui/screens/CharactersScreen.kt", [
    ('"✨ EN ÁLBUM"', '"EN ÁLBUM"'),
    ('"🔒 Bloqueado"', '"BLOQUEADO"')
])

replace_in_file("app/src/main/java/com/example/ui/screens/HomeScreen.kt", [
    ('"🔒 Bloqueado"', '"BLOQUEADO"'),
    ('🎵', 'SÍ'),
    ('🔇', 'NO')
])
