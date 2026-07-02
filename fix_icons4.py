import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Make sure they use Icons.Filled.* instead of androidx.compose.material.icons.Icons.Filled.*
content = content.replace("androidx.compose.material.icons.Icons.Filled.", "Icons.Filled.")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
