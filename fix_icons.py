import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_code = """            androidx.compose.material3.IconButton(
                onClick = { com.example.utils.SoundManager.toggleBgm() },
                modifier = Modifier
                    .background(PaperMarioColors.SkyBlue, CircleShape)
                    .size(40.dp)
            ) {
                Text(if (com.example.utils.SoundManager.isBgmEnabled.value) "SÍ" else "NO", fontSize = 20.sp)
            }
            androidx.compose.material3.IconButton(
                onClick = { showResetWarning = true },
                modifier = Modifier
                    .background(Color(0xFFE67E22), CircleShape)
                    .size(40.dp)
            ) {
                Text("🔄", fontSize = 20.sp)
            }"""

new_code = """            androidx.compose.material3.IconButton(
                onClick = { com.example.utils.SoundManager.toggleBgm() },
                modifier = Modifier
                    .background(PaperMarioColors.SkyBlue, CircleShape)
                    .size(40.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = if (com.example.utils.SoundManager.isBgmEnabled.value) 
                        androidx.compose.material.icons.Icons.Filled.Notifications 
                    else 
                        androidx.compose.material.icons.Icons.Filled.Close,
                    contentDescription = "Toggle BGM",
                    tint = Color.White
                )
            }
            androidx.compose.material3.IconButton(
                onClick = { showResetWarning = true },
                modifier = Modifier
                    .background(Color(0xFFE67E22), CircleShape)
                    .size(40.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Filled.Refresh,
                    contentDescription = "Reset Progress",
                    tint = Color.White
                )
            }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Replaced button texts with Icons")
else:
    print("Could not find button texts to replace")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
