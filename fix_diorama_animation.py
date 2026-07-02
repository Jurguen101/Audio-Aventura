import re

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "r") as f:
    content = f.read()

animation_code = """
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Rotate"
    )

    val walkProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Walk"
    )
"""

content = content.replace(
"""    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Rotate"
    )""", animation_code
)

layer3_code = """        // Layer 3: Main focal character styled as a beautiful Cardboard Standee with white margins and wooden pedestal
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    val walkX = if (walkProgress < 0.5f) {
                        -80f + (walkProgress * 2f) * 160f
                    } else {
                        80f - ((walkProgress - 0.5f) * 2f) * 160f
                    }
                    val directionScale = if (walkProgress < 0.5f) 1f else -1f
                    val bounce = kotlin.math.abs(kotlin.math.sin(walkProgress * kotlin.math.PI * 16).toFloat()) * -30f
                    
                    translationX = walkX
                    translationY = verticalOffset + bounce
                    scaleX = scaleFactor * directionScale
                    scaleY = scaleFactor
                    rotationZ = rotationAngle + (bounce * 0.1f)
                },
            contentAlignment = Alignment.Center
        ) {"""

content = content.replace(
"""        // Layer 3: Main focal character styled as a beautiful Cardboard Standee with white margins and wooden pedestal
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    translationY = verticalOffset
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    rotationZ = rotationAngle
                },
            contentAlignment = Alignment.Center
        ) {""", layer3_code
)

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "w") as f:
    f.write(content)
