sed -i '244i \
        ChunkyGameButton(\
            onClick = { showResetWarning = true },\
            containerColor = Color(0xFFE67E22),\
            bevelColor = Color(0xFFD35400),\
            modifier = Modifier\
                .align(Alignment.TopEnd)\
                .padding(16.dp)\
        ) {\
            Text("🔄 REINICIAR", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)\
        }' app/src/main/java/com/example/ui/screens/HomeScreen.kt
