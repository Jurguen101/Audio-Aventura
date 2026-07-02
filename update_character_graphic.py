import re

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "r") as f:
    content = f.read()

content = content.replace(
    '"📚" -> drawBook(cx, cy, w, h)',
    '"📚" -> drawBook(cx, cy, w, h)\n            "🎒" -> drawBackpack(cx, cy, w, h)'
)

new_func = """
private fun DrawScope.drawBackpack(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Main body
    drawRoundRect(
        color = Color(0xFFF44336),
        topLeft = Offset(cx - r * 0.8f, cy - r * 0.8f),
        size = Size(r * 1.6f, r * 1.8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.4f)
    )
    // Front pocket
    drawRoundRect(
        color = Color(0xFFE53935),
        topLeft = Offset(cx - r * 0.6f, cy + r * 0.2f),
        size = Size(r * 1.2f, r * 0.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Straps
    drawArc(
        color = Color(0xFFD32F2F),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(cx - r * 0.6f, cy - r * 1.2f),
        size = Size(r * 1.2f, r * 1.2f),
        style = Stroke(width = w * 0.05f)
    )
}
"""

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "w") as f:
    f.write(content + new_func)
