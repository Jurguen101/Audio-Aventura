sed -i '186i \
                    Column(\
                        modifier = Modifier.fillMaxHeight(),\
                        verticalArrangement = Arrangement.SpaceEvenly\
                    ) {\
                        repeat(10) {\
                            Box(modifier = Modifier.size(6.dp).background(PaperMarioColors.BorderBrown.copy(alpha = 0.5f), CircleShape))\
                        }\
                    }\
                }' app/src/main/java/com/example/ui/screens/HomeScreen.kt
