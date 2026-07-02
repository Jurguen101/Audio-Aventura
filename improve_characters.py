import re

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "r") as f:
    content = f.read()

# I will replace the character drawing functions with more detailed versions.
# I'll create a new file content and then replace the specific functions.

def replace_func(func_name, new_impl):
    global content
    pattern = r'private fun DrawScope\.' + func_name + r'\(cx: Float, cy: Float, w: Float, h: Float\) \{.*?\n\}(?=\n\n|\nprivate|$)'
    content = re.sub(pattern, new_impl, content, flags=re.DOTALL)

# Detailed Ninja
ninja = """private fun DrawScope.drawNinja(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.4f
    // Shadow under body
    drawCircle(color = Color(0x66000000), radius = r, center = Offset(cx, cy + r * 0.1f))
    // Main body
    drawCircle(color = Color(0xFF1E1E1E), radius = r, center = Offset(cx, cy))
    // Body highlight
    drawArc(color = Color(0x33FFFFFF), startAngle = 180f, sweepAngle = 90f, useCenter = false, topLeft = Offset(cx - r, cy - r), size = androidx.compose.ui.geometry.Size(r * 2, r * 2), style = Stroke(width = w * 0.05f))
    // Skin slit
    drawRoundRect(
        color = Color(0xFFFFDAB9),
        topLeft = Offset(cx - r * 0.7f, cy - r * 0.3f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Skin shadow
    drawRoundRect(
        color = Color(0x33000000),
        topLeft = Offset(cx - r * 0.7f, cy - r * 0.3f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.2f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.12f, center = Offset(cx - r * 0.3f, cy))
    drawCircle(color = Color.Black, radius = r * 0.12f, center = Offset(cx + r * 0.3f, cy))
    // Eye catchlights
    drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(cx - r * 0.35f, cy - r * 0.04f))
    drawCircle(color = Color.White, radius = r * 0.04f, center = Offset(cx + r * 0.25f, cy - r * 0.04f))
    // Red headband
    drawRect(
        color = Color(0xFFD32F2F),
        topLeft = Offset(cx - r, cy - r * 0.65f),
        size = androidx.compose.ui.geometry.Size(r * 2f, r * 0.3f)
    )
    // Headband highlight
    drawLine(color = Color(0xFFE53935), start = Offset(cx - r * 0.8f, cy - r * 0.5f), end = Offset(cx + r * 0.8f, cy - r * 0.5f), strokeWidth = w * 0.04f)
    // Headband knot
    drawCircle(color = Color(0xFFB71C1C), radius = r * 0.2f, center = Offset(cx + r * 0.8f, cy - r * 0.5f))
    drawCircle(color = Color(0xFFD32F2F), radius = r * 0.15f, center = Offset(cx + r * 0.8f, cy - r * 0.5f))
    // Scarf tails
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx + r * 0.8f, cy - r * 0.5f)
        quadraticTo(cx + r * 1.2f, cy - r * 0.2f, cx + r * 1.3f, cy + r * 0.1f)
        quadraticTo(cx + r * 1.0f, cy + r * 0.1f, cx + r * 0.8f, cy - r * 0.5f)
        close()
    }
    drawPath(path, color = Color(0xFFD32F2F))
}"""

# Detailed Ghost
ghost = """private fun DrawScope.drawGhost(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Shadow under ghost
    drawCircle(color = Color(0x22000000), radius = r * 0.8f, center = Offset(cx, cy + r * 1.3f))
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r, cy + r * 0.9f)
        lineTo(cx - r, cy)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(cx - r, cy - r, cx + r, cy + r),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(cx + r, cy + r * 0.9f)
        // Wavy bottom
        val wStep = (r * 2) / 3
        quadraticTo(cx + r - wStep / 2, cy + r * 1.3f, cx + r - wStep, cy + r * 0.9f)
        quadraticTo(cx - r + wStep / 2, cy + r * 0.5f, cx - r, cy + r * 0.9f)
        close()
    }
    drawPath(path, color = Color(0xFFEEEEEE))
    
    // Core shadow
    val shadowPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx, cy + r * 0.9f)
        lineTo(cx, cy)
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(cx - r, cy - r, cx + r, cy + r),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(cx + r, cy + r * 0.9f)
        val wStep = (r * 2) / 3
        quadraticTo(cx + r - wStep / 2, cy + r * 1.3f, cx + r - wStep, cy + r * 0.9f)
        lineTo(cx, cy + r * 0.9f)
        close()
    }
    drawPath(shadowPath, color = Color(0xFFD6D6D6))
    
    // Outline
    drawPath(path, color = Color(0xFFBDBDBD), style = Stroke(width = w * 0.025f))
    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.16f, center = Offset(cx - r * 0.35f, cy - r * 0.1f))
    drawCircle(color = Color.Black, radius = r * 0.16f, center = Offset(cx + r * 0.35f, cy - r * 0.1f))
    // Eye highlights
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.4f, cy - r * 0.15f))
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx + r * 0.3f, cy - r * 0.15f))
    // Blushes
    drawCircle(color = Color(0x44FF4081), radius = r * 0.12f, center = Offset(cx - r * 0.5f, cy + r * 0.1f))
    drawCircle(color = Color(0x44FF4081), radius = r * 0.12f, center = Offset(cx + r * 0.5f, cy + r * 0.1f))
}"""

# Detailed Cowboy
cowboy = """private fun DrawScope.drawCowboy(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Face shadow
    drawCircle(color = Color(0xFFE0C0A0), radius = r, center = Offset(cx, cy + r * 0.1f))
    // Face
    drawCircle(color = Color(0xFFFFDAB9), radius = r, center = Offset(cx, cy))
    // Blushes
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx - r * 0.45f, cy + r * 0.15f))
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx + r * 0.45f, cy + r * 0.15f))
    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.1f, center = Offset(cx - r * 0.3f, cy))
    drawCircle(color = Color.Black, radius = r * 0.1f, center = Offset(cx + r * 0.3f, cy))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx - r * 0.33f, cy - r * 0.03f))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx + r * 0.27f, cy - r * 0.03f))
    // Mustache
    val mustachePath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx, cy + r * 0.15f)
        quadraticTo(cx - r * 0.6f, cy + r * 0.05f, cx - r * 0.7f, cy + r * 0.4f)
        quadraticTo(cx - r * 0.4f, cy + r * 0.3f, cx, cy + r * 0.25f)
        quadraticTo(cx + r * 0.4f, cy + r * 0.3f, cx + r * 0.7f, cy + r * 0.4f)
        quadraticTo(cx + r * 0.6f, cy + r * 0.05f, cx, cy + r * 0.15f)
        close()
    }
    drawPath(mustachePath, color = Color(0xFF4E342E))
    // Hat brim shadow
    drawRoundRect(
        color = Color(0xFF5D4037),
        topLeft = Offset(cx - r * 1.4f, cy - r * 0.45f),
        size = androidx.compose.ui.geometry.Size(r * 2.8f, r * 0.3f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.15f)
    )
    // Hat brim
    drawRoundRect(
        color = Color(0xFF795548),
        topLeft = Offset(cx - r * 1.4f, cy - r * 0.5f),
        size = androidx.compose.ui.geometry.Size(r * 2.8f, r * 0.3f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.15f)
    )
    // Hat top shadow
    drawRoundRect(
        color = Color(0xFF5D4037),
        topLeft = Offset(cx - r * 0.7f, cy - r * 1.05f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.3f)
    )
    // Hat top
    drawRoundRect(
        color = Color(0xFF8D6E63),
        topLeft = Offset(cx - r * 0.7f, cy - r * 1.1f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.3f)
    )
    // Hat band
    drawRect(color = Color(0xFF3E2723), topLeft = Offset(cx - r * 0.7f, cy - r * 0.55f), size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.2f))
    // Gold buckle
    drawRect(color = Color(0xFFFFD700), topLeft = Offset(cx - r * 0.15f, cy - r * 0.6f), size = androidx.compose.ui.geometry.Size(r * 0.3f, r * 0.3f), style = Stroke(width = w * 0.02f))
}"""

# Detailed King
king = """private fun DrawScope.drawKing(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Face shadow
    drawCircle(color = Color(0xFFE0C0A0), radius = r, center = Offset(cx, cy + r * 0.2f))
    // Face
    drawCircle(color = Color(0xFFFFDAB9), radius = r, center = Offset(cx, cy + r * 0.15f))
    // Blushes
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx - r * 0.45f, cy + r * 0.3f))
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx + r * 0.45f, cy + r * 0.3f))
    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.1f, center = Offset(cx - r * 0.3f, cy + r * 0.15f))
    drawCircle(color = Color.Black, radius = r * 0.1f, center = Offset(cx + r * 0.3f, cy + r * 0.15f))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx - r * 0.33f, cy + r * 0.12f))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx + r * 0.27f, cy + r * 0.12f))
    
    // Crown base ermine (white fluff)
    drawRoundRect(
        color = Color.White,
        topLeft = Offset(cx - r * 0.9f, cy - r * 0.2f),
        size = androidx.compose.ui.geometry.Size(r * 1.8f, r * 0.4f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Ermine spots
    drawCircle(color = Color.Black, radius = w * 0.015f, center = Offset(cx - r * 0.5f, cy))
    drawCircle(color = Color.Black, radius = w * 0.015f, center = Offset(cx, cy - r * 0.05f))
    drawCircle(color = Color.Black, radius = w * 0.015f, center = Offset(cx + r * 0.5f, cy))

    // Crown
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 0.85f, cy - r * 0.1f)
        lineTo(cx - r * 1.1f, cy - r * 1.0f)
        lineTo(cx - r * 0.4f, cy - r * 0.4f)
        lineTo(cx, cy - r * 1.2f)
        lineTo(cx + r * 0.4f, cy - r * 0.4f)
        lineTo(cx + r * 1.1f, cy - r * 1.0f)
        lineTo(cx + r * 0.85f, cy - r * 0.1f)
        close()
    }
    // Crown Shadow
    drawPath(path, color = Color(0xFFF57F17))
    // Crown highlight
    drawPath(path, color = Color(0xFFFFD700))
    
    // Jewels
    val drawJewel = { jx: Float, jy: Float, color: Color ->
        drawCircle(color = Color(0x66000000), radius = r * 0.15f, center = Offset(jx, jy + r * 0.02f))
        drawCircle(color = color, radius = r * 0.15f, center = Offset(jx, jy))
        drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(jx - r * 0.05f, jy - r * 0.05f))
    }
    drawJewel(cx - r * 1.1f, cy - r * 1.0f, Color.Red)
    drawJewel(cx, cy - r * 1.2f, Color.Blue)
    drawJewel(cx + r * 1.1f, cy - r * 1.0f, Color.Green)
    drawJewel(cx - r * 0.4f, cy - r * 0.4f, Color(0xFF9C27B0))
    drawJewel(cx + r * 0.4f, cy - r * 0.4f, Color(0xFFFF9800))
}"""

# Detailed Robot
robot = """private fun DrawScope.drawRobot(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Antenna
    drawLine(color = Color.DarkGray, start = Offset(cx, cy - r), end = Offset(cx, cy - r * 1.4f), strokeWidth = w * 0.04f)
    drawCircle(color = Color(0xFFB71C1C), radius = r * 0.2f, center = Offset(cx, cy - r * 1.4f))
    drawCircle(color = Color.Red, radius = r * 0.15f, center = Offset(cx, cy - r * 1.4f))
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.05f, cy - r * 1.45f))
    
    // Ears
    drawRoundRect(color = Color.Gray, topLeft = Offset(cx - r * 1.2f, cy - r * 0.4f), size = androidx.compose.ui.geometry.Size(r * 0.4f, r * 0.8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f))
    drawRoundRect(color = Color.Gray, topLeft = Offset(cx + r * 0.8f, cy - r * 0.4f), size = androidx.compose.ui.geometry.Size(r * 0.4f, r * 0.8f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f))

    // Head Shadow
    drawRoundRect(
        color = Color(0xFF78909C),
        topLeft = Offset(cx - r, cy - r * 0.8f),
        size = androidx.compose.ui.geometry.Size(r * 2f, r * 1.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Head Main
    drawRoundRect(
        color = Color(0xFFB0BEC5),
        topLeft = Offset(cx - r, cy - r * 0.85f),
        size = androidx.compose.ui.geometry.Size(r * 2f, r * 1.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    // Head Highlight
    drawRoundRect(
        color = Color.White.copy(alpha = 0.3f),
        topLeft = Offset(cx - r * 0.8f, cy - r * 0.75f),
        size = androidx.compose.ui.geometry.Size(r * 1.6f, r * 0.2f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f)
    )
    // Screen Face
    drawRoundRect(
        color = Color(0xFF263238),
        topLeft = Offset(cx - r * 0.8f, cy - r * 0.4f),
        size = androidx.compose.ui.geometry.Size(r * 1.6f, r * 1.0f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.1f)
    )
    
    // Eyes
    drawCircle(color = Color.Cyan, radius = r * 0.25f, center = Offset(cx - r * 0.4f, cy - r * 0.1f))
    drawCircle(color = Color.Cyan, radius = r * 0.25f, center = Offset(cx + r * 0.4f, cy - r * 0.1f))
    drawCircle(color = Color.White, radius = r * 0.1f, center = Offset(cx - r * 0.4f, cy - r * 0.1f))
    drawCircle(color = Color.White, radius = r * 0.1f, center = Offset(cx + r * 0.4f, cy - r * 0.1f))
    
    // Mouth (sound wave)
    for (i in -2..2) {
        val hMouth = if (i % 2 == 0) r * 0.1f else r * 0.2f
        drawRect(color = Color.Cyan, topLeft = Offset(cx + i * r * 0.15f - w * 0.015f, cy + r * 0.35f - hMouth / 2), size = androidx.compose.ui.geometry.Size(w * 0.03f, hMouth))
    }
}"""

# Detailed Dragon
dragon = """private fun DrawScope.drawDragon(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Horns
    val hornPathLeft = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 0.4f, cy - r * 0.3f)
        quadraticTo(cx - r * 0.8f, cy - r * 0.8f, cx - r * 1.0f, cy - r * 1.2f)
        quadraticTo(cx - r * 0.4f, cy - r * 1.0f, cx, cy - r * 0.3f)
        close()
    }
    drawPath(hornPathLeft, color = Color(0xFFE65100))
    drawPath(hornPathLeft, color = Color(0xFFFF9800), style = Stroke(width = w * 0.01f))
    
    val hornPathRight = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx + r * 0.4f, cy - r * 0.3f)
        quadraticTo(cx + r * 0.8f, cy - r * 0.8f, cx + r * 1.0f, cy - r * 1.2f)
        quadraticTo(cx + r * 0.4f, cy - r * 1.0f, cx, cy - r * 0.3f)
        close()
    }
    drawPath(hornPathRight, color = Color(0xFFE65100))
    drawPath(hornPathRight, color = Color(0xFFFF9800), style = Stroke(width = w * 0.01f))
    
    // Snout shadow
    drawRoundRect(
        color = Color(0xFF2E7D32),
        topLeft = Offset(cx - r * 0.75f, cy - r * 0.3f),
        size = androidx.compose.ui.geometry.Size(r * 1.5f, r * 1.2f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.4f)
    )
    // Snout main
    drawRoundRect(
        color = Color(0xFF4CAF50),
        topLeft = Offset(cx - r * 0.8f, cy - r * 0.4f),
        size = androidx.compose.ui.geometry.Size(r * 1.6f, r * 1.2f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.4f)
    )
    // Belly/lower jaw
    drawRoundRect(
        color = Color(0xFF81C784),
        topLeft = Offset(cx - r * 0.6f, cy + r * 0.2f),
        size = androidx.compose.ui.geometry.Size(r * 1.2f, r * 0.6f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.2f)
    )
    
    // Nostrils
    drawCircle(color = Color(0xFF1B5E20), radius = r * 0.12f, center = Offset(cx + r * 0.3f, cy + r * 0.4f))
    drawCircle(color = Color(0xFF1B5E20), radius = r * 0.12f, center = Offset(cx + r * 0.6f, cy + r * 0.4f))
    drawCircle(color = Color(0xFF000000), radius = r * 0.05f, center = Offset(cx + r * 0.3f, cy + r * 0.38f))
    drawCircle(color = Color(0xFF000000), radius = r * 0.05f, center = Offset(cx + r * 0.6f, cy + r * 0.38f))
    
    // Eye socket
    drawCircle(color = Color(0xFF388E3C), radius = r * 0.25f, center = Offset(cx - r * 0.2f, cy - r * 0.1f))
    // Eye
    drawCircle(color = Color.Yellow, radius = r * 0.2f, center = Offset(cx - r * 0.2f, cy - r * 0.1f))
    // Pupil
    drawRect(color = Color.Black, topLeft = Offset(cx - r * 0.25f, cy - r * 0.25f), size = androidx.compose.ui.geometry.Size(r * 0.1f, r * 0.3f))
    // Catchlight
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.25f, cy - r * 0.15f))
}"""

# Detailed Pirate
pirate = """private fun DrawScope.drawPirate(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Face shadow
    drawCircle(color = Color(0xFFE0C0A0), radius = r, center = Offset(cx, cy + r * 0.15f))
    // Face
    drawCircle(color = Color(0xFFFFDAB9), radius = r, center = Offset(cx, cy + r * 0.1f))
    
    // Stubble
    for (i in 0..10) {
        val sx = cx - r * 0.5f + (i * r * 0.1f)
        val sy = cy + r * 0.7f + if (i % 2 == 0) r * 0.1f else 0f
        drawCircle(color = Color(0x66000000), radius = w * 0.008f, center = Offset(sx, sy))
    }
    
    // Normal Eye
    drawCircle(color = Color.White, radius = r * 0.15f, center = Offset(cx + r * 0.3f, cy + r * 0.1f))
    drawCircle(color = Color.Black, radius = r * 0.08f, center = Offset(cx + r * 0.3f, cy + r * 0.1f))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx + r * 0.27f, cy + r * 0.07f))
    
    // Eyepatch strap
    drawLine(color = Color.Black, start = Offset(cx - r * 0.9f, cy - r * 0.1f), end = Offset(cx + r * 0.9f, cy + r * 0.3f), strokeWidth = w * 0.03f)
    // Eyepatch
    drawCircle(color = Color.Black, radius = r * 0.22f, center = Offset(cx - r * 0.3f, cy + r * 0.1f))
    
    // Red Bandana
    val bandanaPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r, cy - r * 0.2f)
        quadraticTo(cx, cy - r * 0.6f, cx + r, cy - r * 0.2f)
        lineTo(cx + r, cy - r * 0.4f)
        quadraticTo(cx, cy - r * 0.8f, cx - r, cy - r * 0.4f)
        close()
    }
    drawPath(bandanaPath, color = Color(0xFFD32F2F))
    // Bandana knot
    drawCircle(color = Color(0xFFB71C1C), radius = r * 0.15f, center = Offset(cx - r * 0.9f, cy - r * 0.2f))
    
    // Hat shadow
    val pathS = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 1.5f, cy - r * 0.2f)
        quadraticTo(cx, cy - r * 1.6f, cx + r * 1.5f, cy - r * 0.2f)
        lineTo(cx + r * 1.3f, cy - r * 0.1f)
        lineTo(cx - r * 1.3f, cy - r * 0.1f)
        close()
    }
    drawPath(pathS, color = Color(0xFF111111))
    
    // Hat Main
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 1.4f, cy - r * 0.25f)
        quadraticTo(cx, cy - r * 1.65f, cx + r * 1.4f, cy - r * 0.25f)
        lineTo(cx + r * 1.2f, cy - r * 0.15f)
        lineTo(cx - r * 1.2f, cy - r * 0.15f)
        close()
    }
    drawPath(path, color = Color(0xFF212121))
    drawPath(path, color = Color(0xFF424242), style = Stroke(width = w * 0.02f))
    
    // Skull symbol on hat
    drawCircle(color = Color.White, radius = r * 0.15f, center = Offset(cx, cy - r * 0.6f))
    drawCircle(color = Color.Black, radius = r * 0.04f, center = Offset(cx - r * 0.05f, cy - r * 0.6f))
    drawCircle(color = Color.Black, radius = r * 0.04f, center = Offset(cx + r * 0.05f, cy - r * 0.6f))
    drawLine(color = Color.White, start = Offset(cx - r * 0.25f, cy - r * 0.4f), end = Offset(cx + r * 0.25f, cy - r * 0.8f), strokeWidth = w * 0.03f)
    drawLine(color = Color.White, start = Offset(cx - r * 0.25f, cy - r * 0.8f), end = Offset(cx + r * 0.25f, cy - r * 0.4f), strokeWidth = w * 0.03f)
}"""

# Detailed Astronaut
astronaut = """private fun DrawScope.drawAstronaut(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.4f
    
    // Suit collar
    drawRoundRect(
        color = Color(0xFFB0BEC5),
        topLeft = Offset(cx - r * 1.2f, cy + r * 0.4f),
        size = androidx.compose.ui.geometry.Size(r * 2.4f, r * 1.0f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.4f)
    )
    drawRoundRect(
        color = Color(0xFFCFD8DC),
        topLeft = Offset(cx - r * 1.1f, cy + r * 0.5f),
        size = androidx.compose.ui.geometry.Size(r * 2.2f, r * 0.8f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.3f)
    )
    // Buttons on suit
    drawCircle(color = Color.Red, radius = r * 0.1f, center = Offset(cx - r * 0.5f, cy + r * 0.8f))
    drawCircle(color = Color.Blue, radius = r * 0.1f, center = Offset(cx, cy + r * 0.8f))
    drawCircle(color = Color.Green, radius = r * 0.1f, center = Offset(cx + r * 0.5f, cy + r * 0.8f))

    // Helmet Shadow
    drawCircle(color = Color.LightGray, radius = r, center = Offset(cx, cy + r * 0.05f))
    // Helmet
    drawCircle(color = Color.White, radius = r, center = Offset(cx, cy))
    drawCircle(color = Color(0xFF90A4AE), radius = r, center = Offset(cx, cy), style = Stroke(width = w * 0.05f))
    
    // Visor rim
    drawRoundRect(
        color = Color.DarkGray,
        topLeft = Offset(cx - r * 0.75f, cy - r * 0.45f),
        size = androidx.compose.ui.geometry.Size(r * 1.5f, r * 1.0f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.35f)
    )
    // Visor main
    drawRoundRect(
        color = Color(0xFF0277BD),
        topLeft = Offset(cx - r * 0.7f, cy - r * 0.4f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.9f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.3f)
    )
    // Visor dark gradient/shadow
    drawRoundRect(
        color = Color(0xFF01579B),
        topLeft = Offset(cx - r * 0.7f, cy + r * 0.1f),
        size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 0.4f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.3f)
    )
    
    // Visor curved reflection
    val reflectPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 0.5f, cy - r * 0.2f)
        quadraticTo(cx, cy - r * 0.4f, cx + r * 0.5f, cy - r * 0.2f)
        lineTo(cx + r * 0.4f, cy - r * 0.1f)
        quadraticTo(cx, cy - r * 0.3f, cx - r * 0.4f, cy - r * 0.1f)
        close()
    }
    drawPath(reflectPath, color = Color.White.copy(alpha = 0.6f))
    
    // Star reflection in visor
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.3f, cy))
    drawCircle(color = Color.White, radius = r * 0.02f, center = Offset(cx - r * 0.2f, cy - r * 0.05f))
}"""

# Detailed Squirrel
squirrel = """private fun DrawScope.drawSquirrel(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    // Tail shadow
    drawCircle(color = Color(0xFF5D4037), radius = r * 0.8f, center = Offset(cx + r * 0.8f, cy + r * 0.1f))
    // Tail
    drawCircle(color = Color(0xFF8D6E63), radius = r * 0.8f, center = Offset(cx + r * 0.8f, cy))
    // Tail highlight & texture
    drawCircle(color = Color(0xFFA1887F), radius = r * 0.6f, center = Offset(cx + r * 0.7f, cy - r * 0.1f))
    val tailTuft = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx + r * 1.5f, cy)
        quadraticTo(cx + r * 1.8f, cy - r * 0.5f, cx + r * 1.4f, cy - r * 0.8f)
        quadraticTo(cx + r * 1.2f, cy - r * 0.4f, cx + r * 1.5f, cy)
        close()
    }
    drawPath(tailTuft, color = Color(0xFF8D6E63))

    // Ears
    drawCircle(color = Color(0xFF5D4037), radius = r * 0.3f, center = Offset(cx - r * 0.5f, cy - r * 0.6f))
    drawCircle(color = Color(0xFF5D4037), radius = r * 0.3f, center = Offset(cx + r * 0.5f, cy - r * 0.6f))
    drawCircle(color = Color(0xFFFFCC80), radius = r * 0.15f, center = Offset(cx - r * 0.5f, cy - r * 0.6f))
    drawCircle(color = Color(0xFFFFCC80), radius = r * 0.15f, center = Offset(cx + r * 0.5f, cy - r * 0.6f))
    
    // Face shadow
    drawCircle(color = Color(0xFF6D4C41), radius = r, center = Offset(cx, cy + r * 0.1f))
    // Face
    drawCircle(color = Color(0xFF8D6E63), radius = r, center = Offset(cx, cy))
    
    // Snout
    drawCircle(color = Color(0xFFFFE0B2), radius = r * 0.45f, center = Offset(cx, cy + r * 0.35f))
    
    // Nose
    drawCircle(color = Color(0xFF3E2723), radius = r * 0.12f, center = Offset(cx, cy + r * 0.2f))
    drawCircle(color = Color.White, radius = r * 0.03f, center = Offset(cx - r * 0.03f, cy + r * 0.17f))
    
    // Teeth
    drawRect(color = Color.White, topLeft = Offset(cx - r * 0.15f, cy + r * 0.45f), size = androidx.compose.ui.geometry.Size(r * 0.3f, r * 0.25f))
    drawLine(color = Color.DarkGray, start = Offset(cx, cy + r * 0.45f), end = Offset(cx, cy + r * 0.7f), strokeWidth = w * 0.015f)
    
    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.15f, center = Offset(cx - r * 0.35f, cy - r * 0.1f))
    drawCircle(color = Color.Black, radius = r * 0.15f, center = Offset(cx + r * 0.35f, cy - r * 0.1f))
    // Eye highlights
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.4f, cy - r * 0.15f))
    drawCircle(color = Color.White, radius = r * 0.05f, center = Offset(cx + r * 0.3f, cy - r * 0.15f))
    
    // Whiskers
    drawLine(color = Color.Black, start = Offset(cx - r * 0.4f, cy + r * 0.3f), end = Offset(cx - r * 0.8f, cy + r * 0.2f), strokeWidth = w * 0.01f)
    drawLine(color = Color.Black, start = Offset(cx - r * 0.4f, cy + r * 0.4f), end = Offset(cx - r * 0.8f, cy + r * 0.4f), strokeWidth = w * 0.01f)
    drawLine(color = Color.Black, start = Offset(cx + r * 0.4f, cy + r * 0.3f), end = Offset(cx + r * 0.8f, cy + r * 0.2f), strokeWidth = w * 0.01f)
    drawLine(color = Color.Black, start = Offset(cx + r * 0.4f, cy + r * 0.4f), end = Offset(cx + r * 0.8f, cy + r * 0.4f), strokeWidth = w * 0.01f)
}"""

# Detailed Wizard
wizard = """private fun DrawScope.drawWizard(cx: Float, cy: Float, w: Float, h: Float) {
    val r = w * 0.35f
    
    // Beard shadow
    val beardShadow = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 1.1f, cy)
        lineTo(cx + r * 1.1f, cy)
        lineTo(cx, cy + r * 1.7f)
        close()
    }
    drawPath(beardShadow, color = Color(0xFFBDBDBD))
    
    // Beard main
    val beardPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r, cy)
        lineTo(cx + r, cy)
        lineTo(cx, cy + r * 1.6f)
        close()
    }
    drawPath(beardPath, color = Color(0xFFEEEEEE))
    
    // Beard texture lines
    for (i in 1..4) {
        drawLine(color = Color(0xFFE0E0E0), start = Offset(cx - r + i * r * 0.4f, cy + r * 0.2f), end = Offset(cx, cy + r * 1.4f), strokeWidth = w * 0.02f)
    }

    // Face
    drawCircle(color = Color(0xFFFFDAB9), radius = r * 0.8f, center = Offset(cx, cy - r * 0.2f))
    
    // Blushes
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx - r * 0.4f, cy))
    drawCircle(color = Color(0x33FF0000), radius = r * 0.15f, center = Offset(cx + r * 0.4f, cy))

    // Glasses
    drawCircle(color = Color(0x33FFFFFF), radius = r * 0.2f, center = Offset(cx - r * 0.3f, cy - r * 0.2f))
    drawCircle(color = Color(0xFFD4AF37), radius = r * 0.2f, center = Offset(cx - r * 0.3f, cy - r * 0.2f), style = Stroke(width = w * 0.02f))
    drawCircle(color = Color(0x33FFFFFF), radius = r * 0.2f, center = Offset(cx + r * 0.3f, cy - r * 0.2f))
    drawCircle(color = Color(0xFFD4AF37), radius = r * 0.2f, center = Offset(cx + r * 0.3f, cy - r * 0.2f), style = Stroke(width = w * 0.02f))
    drawLine(color = Color(0xFFD4AF37), start = Offset(cx - r * 0.1f, cy - r * 0.2f), end = Offset(cx + r * 0.1f, cy - r * 0.2f), strokeWidth = w * 0.02f)

    // Eyes
    drawCircle(color = Color.Black, radius = r * 0.06f, center = Offset(cx - r * 0.3f, cy - r * 0.2f))
    drawCircle(color = Color.Black, radius = r * 0.06f, center = Offset(cx + r * 0.3f, cy - r * 0.2f))
    
    // Hat Shadow
    val hatPathS = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 1.3f, cy - r * 0.3f)
        lineTo(cx + r * 1.3f, cy - r * 0.3f)
        lineTo(cx, cy - r * 2.1f)
        close()
    }
    drawPath(hatPathS, color = Color(0xFF283593))
    
    // Hat Main
    val hatPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(cx - r * 1.2f, cy - r * 0.4f)
        lineTo(cx + r * 1.2f, cy - r * 0.4f)
        lineTo(cx, cy - r * 2.0f)
        close()
    }
    drawPath(hatPath, color = Color(0xFF3F51B5))
    
    // Stars on hat
    val drawStar = { sx: Float, sy: Float, sr: Float ->
        val path = androidx.compose.ui.graphics.Path()
        for (i in 0 until 10) {
            val radius = if (i % 2 == 0) sr else sr * 0.4f
            val angle = (Math.PI * 2 * i / 10) - Math.PI / 2
            val x = sx + radius * Math.cos(angle).toFloat()
            val y = sy + radius * Math.sin(angle).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        drawPath(path, color = Color(0xFFFFD700))
    }
    drawStar(cx - r * 0.3f, cy - r * 0.8f, r * 0.15f)
    drawStar(cx + r * 0.4f, cy - r * 1.2f, r * 0.1f)
    drawStar(cx, cy - r * 1.6f, r * 0.12f)

    // Hat brim
    drawRoundRect(
        color = Color(0xFF303F9F),
        topLeft = Offset(cx - r * 1.4f, cy - r * 0.5f),
        size = androidx.compose.ui.geometry.Size(r * 2.8f, r * 0.25f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.12f)
    )
}"""

replace_func('drawNinja', ninja)
replace_func('drawGhost', ghost)
replace_func('drawCowboy', cowboy)
replace_func('drawKing', king)
replace_func('drawRobot', robot)
replace_func('drawDragon', dragon)
replace_func('drawPirate', pirate)
replace_func('drawAstronaut', astronaut)
replace_func('drawSquirrel', squirrel)
replace_func('drawWizard', wizard)

with open("app/src/main/java/com/example/ui/components/CharacterGraphic.kt", "w") as f:
    f.write(content)
