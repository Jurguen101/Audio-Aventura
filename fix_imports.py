import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

if "import androidx.compose.material.icons" not in content:
    content = content.replace(
        "import androidx.compose.material3.Text",
        "import androidx.compose.material3.Text\nimport androidx.compose.material.icons.Icons\nimport androidx.compose.material.icons.filled.Notifications\nimport androidx.compose.material.icons.filled.Close\nimport androidx.compose.material.icons.filled.Refresh"
    )
    with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
        f.write(content)
