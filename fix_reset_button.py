import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_button = """        // Top Right Reset Button
        ChunkyGameButton(
            onClick = { showResetWarning = true },
            containerColor = Color(0xFFE67E22),
            bevelColor = Color(0xFFD35400),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("🔄", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 4.dp))
        }"""

new_button = """        // Top Right Reset Button
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

if old_button in content:
    content = content.replace(old_button, new_button)
    with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
        f.write(content)
    print("Reset button updated.")
else:
    print("Failed to find reset button.")
