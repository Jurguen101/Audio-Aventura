import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_button = """        // Top Right Reset Button
        androidx.compose.material3.IconButton(
            onClick = { showResetWarning = true },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color(0xFFE67E22), CircleShape)
                .size(40.dp)
        ) {
            Text("🔄", fontSize = 20.sp)
        }"""

new_button = """        // Top Right Action Buttons
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            androidx.compose.material3.IconButton(
                onClick = { com.example.utils.SoundManager.toggleBgm() },
                modifier = Modifier
                    .background(PaperMarioColors.SkyBlue, CircleShape)
                    .size(40.dp)
            ) {
                Text(if (com.example.utils.SoundManager.isBgmEnabled.value) "🎵" else "🔇", fontSize = 20.sp)
            }
            androidx.compose.material3.IconButton(
                onClick = { showResetWarning = true },
                modifier = Modifier
                    .background(Color(0xFFE67E22), CircleShape)
                    .size(40.dp)
            ) {
                Text("🔄", fontSize = 20.sp)
            }
        }"""

if old_button in content:
    content = content.replace(old_button, new_button)
    print("Updated button")
else:
    print("Could not find button")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
