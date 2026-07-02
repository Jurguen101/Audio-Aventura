import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Edit 1: page count
old_pager_state = """    val pagerState = rememberPagerState(pageCount = { chapters.size })"""
new_pager_state = """    val totalPages = if (chapters.isNotEmpty()) chapters.size + 1 else 0
    val pagerState = rememberPagerState(pageCount = { totalPages })"""
if old_pager_state in content:
    content = content.replace(old_pager_state, new_pager_state)
    print("Updated pager state")
else:
    print("Could not find pager state")

# Edit 2: HorizontalPager content
old_pager_content = """                    if (chapters.isNotEmpty()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            val chapter = chapters[page]
                            DioramaPage(
                                chapter = chapter,
                                onPlayClick = {
                                    selectedChapterForSynopsis = chapter
                                }
                            )
                        }
                    }"""

new_pager_content = """                    if (chapters.isNotEmpty()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            if (page < chapters.size) {
                                val chapter = chapters[page]
                                DioramaPage(
                                    chapter = chapter,
                                    onPlayClick = {
                                        selectedChapterForSynopsis = chapter
                                    }
                                )
                            } else {
                                val allCompleted = chapters.isNotEmpty() && chapters.all { it.completed }
                                CreditsTriggerPage(
                                    allCompleted = allCompleted,
                                    onTriggerClick = { viewModel.navigateTo(Screen.Credits) }
                                )
                            }
                        }
                    }"""
if old_pager_content in content:
    content = content.replace(old_pager_content, new_pager_content)
    print("Updated pager content")
else:
    print("Could not find pager content")

# Edit 3: Pager indicators
old_indicators = """            if (chapters.isNotEmpty()) {
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(chapters.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) PaperMarioColors.StageRed else PaperMarioColors.BorderBrown.copy(alpha = 0.3f)"""

new_indicators = """            if (chapters.isNotEmpty()) {
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(totalPages) { iteration ->
                        val color = if (pagerState.currentPage == iteration) PaperMarioColors.StageRed else PaperMarioColors.BorderBrown.copy(alpha = 0.3f)"""

if old_indicators in content:
    content = content.replace(old_indicators, new_indicators)
    print("Updated indicators")
else:
    print("Could not find indicators")

# Add the new composable
new_composable = """@Composable
fun CreditsTriggerPage(allCompleted: Boolean, onTriggerClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaperBanner(
            text = "CRÉDITOS",
            backgroundColor = PaperMarioColors.StageRed,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (allCompleted) {
                CardboardContainer(backgroundColor = PaperMarioColors.SkyBlue) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "🎉 ¡Felicidades! 🎉",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Has completado todos los cuentos.",
                            fontSize = 16.sp,
                            color = PaperMarioColors.BorderBrown,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ChunkyGameButton(
                            onClick = onTriggerClick,
                            containerColor = PaperMarioColors.StageRed,
                            bevelColor = Color(0xFF7F0000)
                        ) {
                            Text("VER CRÉDITOS", fontSize = 20.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                }
            } else {
                CardboardContainer(backgroundColor = Color(0xFFEBE5DB)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "🔒 Bloqueado",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Termina los 10 cuentos\\npara ver los créditos.",
                            fontSize = 16.sp,
                            color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
"""

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content + "\n" + new_composable)

