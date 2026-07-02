import re

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "r") as f:
    content = f.read()

# Add DioramaThemeColors and getTheme function just before PopUpDioramaCanvas
theme_code = """
data class DioramaThemeColors(
    val groundColor: Color,
    val spotlightColor: Color,
    val curtainColor: Color,
    val headerTitle: String
)

fun getDioramaTheme(mainEmoji: String): DioramaThemeColors {
    return when (mainEmoji) {
        "🥷" -> DioramaThemeColors(Color(0xFF8D6E63), Color(0xFFE57373).copy(alpha = 0.15f), Color(0xFFB71C1C), "DOJO SECRETO") // Ninja
        "👻" -> DioramaThemeColors(Color(0xFF455A64), Color(0xFFCE93D8).copy(alpha = 0.15f), Color(0xFF4A148C), "MANSIÓN EMBRUJADA") // Fantasma
        "🤠" -> DioramaThemeColors(Color(0xFFD7CCC8), Color(0xFFFFCC80).copy(alpha = 0.15f), Color(0xFFE65100), "LEJANO OESTE") // Vaquero
        "👑" -> DioramaThemeColors(Color(0xFFB71C1C), Color(0xFFFFF59D).copy(alpha = 0.2f), Color(0xFF1A237E), "SALÓN DEL TRONO") // Rey
        "🤖" -> DioramaThemeColors(Color(0xFF90A4AE), Color(0xFF80DEEA).copy(alpha = 0.2f), Color(0xFF37474F), "LABORATORIO") // Robot
        "🐉" -> DioramaThemeColors(Color(0xFF4E342E), Color(0xFFFFAB91).copy(alpha = 0.15f), Color(0xFFBF360C), "CUEVA DEL DRAGÓN") // Dragón
        "🏴‍☠️" -> DioramaThemeColors(Color(0xFF795548), Color(0xFF90CAF9).copy(alpha = 0.15f), Color(0xFF006064), "BARCO PIRATA") // Pirata
        "🧑‍🚀" -> DioramaThemeColors(Color(0xFFCFD8DC), Color(0xFFB39DDB).copy(alpha = 0.15f), Color(0xFF283593), "LUNA EXTERIOR") // Astronauta
        "🐿️" -> DioramaThemeColors(Color(0xFFA5D6A7), Color(0xFFFFF59D).copy(alpha = 0.15f), Color(0xFF2E7D32), "BOSQUE MÁGICO") // Ardilla
        "🧙‍♂️" -> DioramaThemeColors(Color(0xFF512DA8), Color(0xFFCE93D8).copy(alpha = 0.2f), Color(0xFF311B92), "TORRE DEL MAGO") // Mago
        else -> DioramaThemeColors(Color(0xFFC0A473).copy(alpha = 0.8f), Color(0xFFFFF59D).copy(alpha = 0.15f), PaperMarioColors.StageRed, "ESCENARIO DE AVENTURAS")
    }
}

fun PopUpDioramaCanvas("""

content = content.replace("fun PopUpDioramaCanvas(", theme_code)

# Replace ground color
content = content.replace(
    "color = Color(0xFFC0A473).copy(alpha = 0.8f), // Real cardboard brown ground layer",
    "color = getDioramaTheme(mainEmoji).groundColor,"
)

# Replace spotlight colors
content = content.replace(
    "color = Color(0xFFFFF59D).copy(alpha = 0.15f) // Warm golden theatrical glow",
    "color = getDioramaTheme(mainEmoji).spotlightColor"
)
content = content.replace(
    "color = Color(0xFFFFF59D).copy(alpha = 0.15f)",
    "color = getDioramaTheme(mainEmoji).spotlightColor"
)

# Replace curtain colors
content = content.replace(
    ".background(PaperMarioColors.StageRed)",
    ".background(getDioramaTheme(mainEmoji).curtainColor)"
)

# Replace text
content = content.replace(
    'text = "🎭 ESCENARIO DE AVENTURAS 🎭"',
    'text = getDioramaTheme(mainEmoji).headerTitle'
)

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "w") as f:
    f.write(content)

