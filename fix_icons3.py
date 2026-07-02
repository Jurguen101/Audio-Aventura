import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

content = content.replace(
    "androidx.compose.material.icons.filled.Notifications",
    "androidx.compose.material.icons.Icons.Filled.Notifications"
)
content = content.replace(
    "androidx.compose.material.icons.filled.Close",
    "androidx.compose.material.icons.Icons.Filled.Close"
)
content = content.replace(
    "androidx.compose.material.icons.filled.Refresh",
    "androidx.compose.material.icons.Icons.Filled.Refresh"
)
content = content.replace(
    "androidx.compose.material.icons.filled.Star",
    "androidx.compose.material.icons.Icons.Filled.Star"
)
content = content.replace(
    "androidx.compose.material.icons.filled.StarHalf",
    "androidx.compose.material.icons.Icons.Filled.StarHalf"
)
content = content.replace(
    "androidx.compose.material.icons.filled.ShoppingCart",
    "androidx.compose.material.icons.Icons.Filled.ShoppingCart"
)

if "import androidx.compose.material.icons.Icons" not in content:
    content = content.replace("import androidx.compose.material3.Text", "import androidx.compose.material3.Text\nimport androidx.compose.material.icons.Icons")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
