import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Edit 1 & 2: Add state and AlertDialog
old_state_block = """    var showResetWarning by remember { mutableStateOf(false) }

    if (showResetWarning) {"""

new_state_block = """    var showResetWarning by remember { mutableStateOf(false) }
    var selectedChapterForSynopsis by remember { mutableStateOf<StoryChapter?>(null) }

    if (selectedChapterForSynopsis != null) {
        val chapter = selectedChapterForSynopsis!!
        AlertDialog(
            onDismissRequest = { selectedChapterForSynopsis = null },
            title = { Text(text = chapter.title, fontWeight = FontWeight.Bold, color = PaperMarioColors.StageRed) },
            text = { Text(text = chapter.description, color = PaperMarioColors.BorderBrown) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.startChapter(chapter.id)
                    viewModel.navigateTo(Screen.Story)
                    selectedChapterForSynopsis = null
                }) {
                    Text("Aceptar", color = PaperMarioColors.StageRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedChapterForSynopsis = null }) {
                    Text("Declinar", color = PaperMarioColors.BorderBrown, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = PaperMarioColors.PaperWhite
        )
    }

    if (showResetWarning) {"""

if old_state_block in content:
    content = content.replace(old_state_block, new_state_block)
    print("State block updated.")
else:
    print("Failed to update state block.")

# Edit 3: Update DioramaPage onPlayClick in HomeScreen
old_onplay = """                            DioramaPage(
                                chapter = chapter,
                                onPlayClick = {
                                    viewModel.startChapter(chapter.id)
                                    viewModel.navigateTo(Screen.Story)
                                }
                            )"""

new_onplay = """                            DioramaPage(
                                chapter = chapter,
                                onPlayClick = {
                                    selectedChapterForSynopsis = chapter
                                }
                            )"""

if old_onplay in content:
    content = content.replace(old_onplay, new_onplay)
    print("onPlayClick updated.")
else:
    print("Failed to update onPlayClick.")

# Edit 4: Update REINICIAR button
old_button = """        // Top Right Reset Button
        ChunkyGameButton(
            onClick = { showResetWarning = true },
            containerColor = Color(0xFFE67E22),
            bevelColor = Color(0xFFD35400),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("🔄 REINICIAR", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }"""

new_button = """        // Top Right Reset Button
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

if old_button in content:
    content = content.replace(old_button, new_button)
    print("REINICIAR button updated.")
else:
    print("Failed to update REINICIAR button.")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)

