import re

with open("app/src/main/java/com/example/ui/screens/StoryPreviewScreen.kt", "r") as f:
    content = f.read()

old_code_1 = """                                                // Lock / Unlock symbol
                                                Text(
                                                    text = if (currentChapter.unlocked) currentChapter.emoji else "🔒",
                                                    fontSize = 24.sp
                                                )"""
new_code_1 = """                                                // Lock / Unlock symbol
                                                Box(modifier = Modifier.size(32.dp)) {
                                                    com.example.ui.components.CharacterGraphic(
                                                        emoji = if (currentChapter.unlocked) currentChapter.emoji else "🔒"
                                                    )
                                                }"""

if old_code_1 in content:
    content = content.replace(old_code_1, new_code_1)
    print("Replaced first StoryPreviewScreen Text with CharacterGraphic")
else:
    print("Could not find first StoryPreviewScreen Text")


old_code_2 = """                                        Text(
                                            text = if (currentChapter.unlocked) currentChapter.emoji else "🔒",
                                            fontSize = 24.sp
                                        )"""
new_code_2 = """                                        Box(modifier = Modifier.size(32.dp)) {
                                            com.example.ui.components.CharacterGraphic(
                                                emoji = if (currentChapter.unlocked) currentChapter.emoji else "🔒"
                                            )
                                        }"""
if old_code_2 in content:
    content = content.replace(old_code_2, new_code_2)
    print("Replaced second StoryPreviewScreen Text with CharacterGraphic")
else:
    print("Could not find second StoryPreviewScreen Text")

with open("app/src/main/java/com/example/ui/screens/StoryPreviewScreen.kt", "w") as f:
    f.write(content)
