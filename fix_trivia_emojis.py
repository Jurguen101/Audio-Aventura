import re

with open("app/src/main/java/com/example/ui/screens/TriviaScreen.kt", "r") as f:
    content = f.read()

content = content.replace('"🎓 DESAFÍO DE PREGUNTAS DE CARTÓN 🎓"', '"DESAFÍO DE PREGUNTAS DE CARTÓN"')
content = content.replace('"🏠 SALIR"', '"SALIR"')
content = content.replace('text = if (isStarEarned) "⭐" else "⚫"', 'text = if (isStarEarned) "★" else "○"')
content = content.replace('"¡VERIFICAR RESPUESTA! 🚀"', '"¡VERIFICAR RESPUESTA!"')
content = content.replace('"🎉 ¡EXCELENTE! 🎉"', '"¡EXCELENTE!"')
content = content.replace('"FINALIZAR AVENTURA 🎓🏆"', '"FINALIZAR AVENTURA"')
content = content.replace('"SIGUIENTE PREGUNTA ▶"', '"SIGUIENTE PREGUNTA"')
content = content.replace('"❌ ¡OH, CASI LO LOGRAS! ❌"', '"¡OH, CASI LO LOGRAS!"')
content = content.replace('"CONTINUAR 🏁"', '"CONTINUAR"')
content = content.replace('"CONTINUAR ▶"', '"CONTINUAR"')

with open("app/src/main/java/com/example/ui/screens/TriviaScreen.kt", "w") as f:
    f.write(content)
