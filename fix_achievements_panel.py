import re

with open("app/src/main/java/com/example/ui/components/AchievementsPanel.kt", "r") as f:
    content = f.read()

old_code = """            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (unlocked) achievement.emoji else "🔒",
                    fontSize = 24.sp
                )
            }"""

new_code = """            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape)
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                CharacterGraphic(
                    emoji = if (unlocked) achievement.emoji else "🔒",
                    modifier = Modifier.fillMaxSize()
                )
            }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Replaced Achievement Text with CharacterGraphic")
else:
    print("Could not find Achievement Text")

with open("app/src/main/java/com/example/ui/components/AchievementsPanel.kt", "w") as f:
    f.write(content)
