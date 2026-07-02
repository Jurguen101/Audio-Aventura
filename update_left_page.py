import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_code = """                        // Top Stats
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                                    Text("⭐ ", fontSize = 24.sp)
                                    Text("$starsCount", fontSize = 24.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                                    Text("🃏 ", fontSize = 24.sp)
                                    Text("$wildcardsCount", fontSize = 24.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                            
                            ChunkyGameButton(
                                onClick = { viewModel.navigateTo(Screen.Characters) },
                                containerColor = Color(0xFFD32F2F),
                                bevelColor = Color(0xFF9A0007),
                            ) {
                                Text("🏪 TIENDA", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
                            }
                        }"""

new_code = """                        // Top Stats
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        ) {
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    Text("⭐ ", fontSize = 20.sp)
                                    Text("$starsCount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                            CardboardContainer(backgroundColor = Color.White, hasStitches = false) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                    Text("🃏 ", fontSize = 20.sp)
                                    Text("$wildcardsCount", fontSize = 20.sp, fontWeight = FontWeight.Black, color = PaperMarioColors.BorderBrown)
                                }
                            }
                        }

                        // Beautiful Tienda Button
                        ChunkyGameButton(
                            onClick = { viewModel.navigateTo(Screen.Characters) },
                            containerColor = Color(0xFFF1C40F),
                            bevelColor = Color(0xFFF39C12),
                            borderColor = Color(0xFFD35400),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, bottom = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("🏪", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
                                Text("TIENDA DE MASCOTAS", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                        }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
        f.write(content)
    print("Successfully replaced.")
else:
    print("Could not find the old code.")
