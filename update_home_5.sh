sed -i 's/\.align(Alignment\.TopEnd)/\.align(Alignment.BottomStart)/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i '/modifier = Modifier\.height(48\.dp)/d' app/src/main/java/com/example/ui/screens/HomeScreen.kt
sed -i 's/Text("🏪 TIENDA", color = Color\.White, fontWeight = FontWeight\.Black, fontSize = 14\.sp)/Text("🏪 TIENDA", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))/g' app/src/main/java/com/example/ui/screens/HomeScreen.kt
