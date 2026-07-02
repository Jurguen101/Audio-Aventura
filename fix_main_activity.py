import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Add import
old_imports = "import com.example.ui.screens.TriviaScreen\nimport com.example.ui.theme.MyApplicationTheme"
new_imports = "import com.example.ui.screens.TriviaScreen\nimport com.example.ui.screens.FinalCreditsScreen\nimport com.example.ui.theme.MyApplicationTheme"

if old_imports in content:
    content = content.replace(old_imports, new_imports)
    print("Added import.")
else:
    print("Could not find import location.")

# Add to when block
old_when = """                                    Screen.Credits -> com.example.ui.screens.CreditsScreen(viewModel = viewModel)
                                    Screen.Home -> HomeScreen(viewModel = viewModel)"""
new_when = """                                    Screen.Credits -> com.example.ui.screens.CreditsScreen(viewModel = viewModel)
                                    Screen.FinalCredits -> FinalCreditsScreen(viewModel = viewModel)
                                    Screen.Home -> HomeScreen(viewModel = viewModel)"""

if old_when in content:
    content = content.replace(old_when, new_when)
    print("Added to when block.")
else:
    print("Could not find when block.")

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
