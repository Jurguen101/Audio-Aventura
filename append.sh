# Remove the last two } 
sed -i '$d' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i '$d' app/src/main/java/com/example/ui/screens/HomeScreen.kt

cat << 'INNER_EOF' >> app/src/main/java/com/example/ui/screens/HomeScreen.kt
            } // Closes the Row

            // Pager Indicators
            if (chapters.isNotEmpty()) {
                Row(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 8.dp, end = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(chapters.size) { iteration ->
                        val color = if (pagerState.currentPage == iteration) PaperMarioColors.StageRed else PaperMarioColors.BorderBrown.copy(alpha = 0.3f)
                        Box(
                            modifier = Modifier
                                .padding(2.dp)
                                .size(12.dp)
                                .background(color, CircleShape)
                        )
                    }
                }
            }
        } // Closes the Inner Box

        // Top Right Reset Button
        ChunkyGameButton(
            onClick = { showResetWarning = true },
            containerColor = Color(0xFFE67E22),
            bevelColor = Color(0xFFD35400),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("🔄 REINICIAR", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    } // Closes the Outer Box
} // Closes HomeScreen function

@Composable
fun DioramaPage(chapter: StoryChapter, onPlayClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "diorama_animation")
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        PaperBanner(
            text = chapter.title.uppercase(),
            backgroundColor = PaperMarioColors.StageRed,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 2.5D Diorama Scene
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .graphicsLayer {
                    rotationX = 15f
                    cameraDistance = 8 * density
                },
            contentAlignment = Alignment.Center
        ) {
            // Background Sky / Scene
            CardboardContainer(
                modifier = Modifier.fillMaxSize(),
                backgroundColor = PaperMarioColors.SkyBlue,
                hasStitches = false
            ) {
                // Clouds
                Text(
                    "☁️",
                    fontSize = 60.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = 20.dp + cloudOffset.dp, y = 20.dp)
                        .graphicsLayer { shadowElevation = 10f }
                )
                Text(
                    "☁️",
                    fontSize = 50.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-30).dp - cloudOffset.dp, y = 40.dp)
                        .graphicsLayer { shadowElevation = 15f }
                )

                // Ground
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .background(PaperMarioColors.GrassGreen)
                )

                // Decor (Trees, Houses)
                Text(
                    "🌲",
                    fontSize = 70.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 30.dp, y = (-20).dp)
                        .graphicsLayer { shadowElevation = 20f }
                )
                
                Text(
                    "🏠",
                    fontSize = 80.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-20).dp, y = (-10).dp)
                        .graphicsLayer { shadowElevation = 25f }
                )

                // Main Character
                StickerCharacter(
                    emoji = if (chapter.unlocked) chapter.emoji else "🔒",
                    size = 130.dp,
                    hasPedestal = true,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 20.dp)
                        .graphicsLayer { 
                            shadowElevation = 40f 
                        }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        if (chapter.unlocked) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) { idx ->
                    Text(
                        text = if (idx < chapter.starsEarned) "⭐" else "☆",
                        fontSize = 24.sp,
                        color = if (idx < chapter.starsEarned) Color(0xFFFFC107) else Color(0x55000000)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ChunkyGameButton(
                onClick = onPlayClick,
                containerColor = PaperMarioColors.StageRed,
                bevelColor = Color(0xFF7F0000)
            ) {
                Text("▶ JUGAR", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
            }
        } else {
            CardboardContainer(backgroundColor = Color(0xFFEBE5DB)) {
                Text(
                    "Bloqueado\n¡Supera el nivel anterior!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = PaperMarioColors.BorderBrown.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ChunkyGameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF4CAF50),
    bevelColor: Color = Color(0xFF2E7D32),
    borderColor: Color = PaperMarioColors.BorderBrown,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val alpha = if (enabled) 1f else 0.5f
    Box(
        modifier = modifier
            .clickable(enabled = enabled, onClick = {
                com.example.utils.SoundManager.playPop()
                onClick()
            })
            .padding(bottom = 5.dp) // space for bevel
    ) {
        // Shadow/Bevel underneath
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 5.dp)
                .background(bevelColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .border(2.5.dp, borderColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
        )
        // Main button face
        Box(
            modifier = Modifier
                .background(containerColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .border(2.5.dp, borderColor.copy(alpha = alpha), RoundedCornerShape(14.dp))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}
INNER_EOF
