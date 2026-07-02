import re

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "r") as f:
    content = f.read()

old_code = """                Text(
                    text = emoji,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .align(alignment)
                        .padding(16.dp)
                        .rotate(rotationAngle * (index + 1) * 0.5f)
                )"""

new_code = """                Box(
                    modifier = Modifier
                        .align(alignment)
                        .padding(16.dp)
                        .size(32.dp)
                        .rotate(rotationAngle * (index + 1) * 0.5f)
                ) {
                    // Draw geometric shape as background prop instead of emoji
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val shapeType = index % 3
                        val color = when (index % 4) {
                            0 -> Color(0xFFFFD700) // Gold
                            1 -> Color(0xFFFF5722) // Deep Orange
                            2 -> Color(0xFF4CAF50) // Green
                            else -> Color(0xFF03A9F4) // Blue
                        }
                        when (shapeType) {
                            0 -> drawCircle(color = color, radius = size.width / 2f)
                            1 -> drawRect(color = color, size = androidx.compose.ui.geometry.Size(size.width, size.height))
                            2 -> {
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(size.width / 2f, 0f)
                                    lineTo(size.width, size.height)
                                    lineTo(0f, size.height)
                                    close()
                                }
                                drawPath(path, color = color)
                            }
                        }
                    }
                }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Replaced floating emoji Text with generic Canvas graphics")
else:
    print("Could not find floating emoji Text")

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "w") as f:
    f.write(content)
