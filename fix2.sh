sed -i '186,195d' app/src/main/java/com/example/ui/screens/HomeScreen.kt
cat << 'INNER_EOF' >> temp_insert.txt
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        repeat(10) {
                            Box(modifier = Modifier.size(6.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.5f), CircleShape))
                        }
                    }
                }
INNER_EOF
sed -i '185r temp_insert.txt' app/src/main/java/com/example/ui/screens/HomeScreen.kt
rm temp_insert.txt
