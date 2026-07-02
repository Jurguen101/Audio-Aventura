import re

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "r") as f:
    content = f.read()

# Fix the detached @Composable
content = content.replace(
"""@Composable

data class DioramaThemeColors""",
"""data class DioramaThemeColors"""
)

content = content.replace(
"""fun PopUpDioramaCanvas""",
"""@Composable
fun PopUpDioramaCanvas"""
)

with open("app/src/main/java/com/example/ui/components/DioramaComponents.kt", "w") as f:
    f.write(content)
