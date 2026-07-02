import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Replace star
content = content.replace('Text("⭐ ", fontSize = 20.sp)', 'androidx.compose.material3.Icon(androidx.compose.material.icons.filled.Star, contentDescription="Star", tint=Color(0xFFFFD700), modifier=Modifier.size(24.dp))')

# Replace wildcard
content = content.replace('Text("🃏 ", fontSize = 20.sp)', 'androidx.compose.material3.Icon(androidx.compose.material.icons.filled.StarHalf, contentDescription="Wildcard", tint=Color(0xFF9C27B0), modifier=Modifier.size(24.dp))')

# Replace store
content = content.replace('Text("🏪", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))', 'androidx.compose.material3.Icon(androidx.compose.material.icons.filled.ShoppingCart, contentDescription="Store", tint=Color.White, modifier=Modifier.size(28.dp).padding(end = 12.dp))')

# Replace play button
content = content.replace('Text("▶ JUGAR", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)', 'Text("JUGAR", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)')

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
