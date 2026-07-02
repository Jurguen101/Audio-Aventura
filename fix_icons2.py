import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "androidx.compose.material.icons.Icons.Filled.Notifications",
    "androidx.compose.material.icons.filled.Notifications"
)
content = content.replace(
    "androidx.compose.material.icons.Icons.Filled.Close",
    "androidx.compose.material.icons.filled.Close"
)
content = content.replace(
    "androidx.compose.material.icons.Icons.Filled.Refresh",
    "androidx.compose.material.icons.filled.Refresh"
)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
