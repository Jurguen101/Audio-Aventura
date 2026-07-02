import re

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "r") as f:
    content = f.read()

old_code = """            else -> {
                // Fallback: draw a colored circle with a question mark
                drawCircle(color = Color.LightGray, radius = w * 0.4f, center = Offset(cx, cy))
            }"""

new_code = """            else -> {
                // Generic cardboard prop for unmatched emojis
                drawRoundRect(
                    color = Color(0xFFC0A473),
                    topLeft = Offset(cx - w * 0.3f, cy - h * 0.3f),
                    size = Size(w * 0.6f, h * 0.6f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.1f)
                )
                drawRoundRect(
                    color = Color(0xFF8D6E63),
                    topLeft = Offset(cx - w * 0.2f, cy - h * 0.2f),
                    size = Size(w * 0.4f, h * 0.4f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.05f)
                )
            }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Replaced fallback")
else:
    print("Could not find fallback")

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "w") as f:
    f.write(content)
