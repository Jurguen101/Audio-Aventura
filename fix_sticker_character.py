import re

with open("app/src/main/java/com/example/ui/components/CardboardComponents.kt", "r") as f:
    content = f.read()

old_code = """        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                // Layer 1: Sticker Shadow
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size / 3),
                    clip = false
                )
                // Layer 2: White paper cutout background
                .background(Color.White, RoundedCornerShape(size / 3))
                // Layer 3: Thick stylized black pencil border
                .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(size / 3))
                .padding(4.dp)
        ) {
            Text(
                text = emoji,
                fontSize = (size.value * 0.55f).sp,
                textAlign = TextAlign.Center
            )
        }"""

new_code = """        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                // Layer 1: Sticker Shadow
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(size / 3),
                    clip = false
                )
                // Layer 2: White paper cutout background
                .background(Color.White, RoundedCornerShape(size / 3))
                // Layer 3: Thick stylized black pencil border
                .border(2.5.dp, PaperMarioColors.BorderBrown, RoundedCornerShape(size / 3))
                .padding(12.dp) // Adjusted padding for CharacterGraphic
        ) {
            CharacterGraphic(
                emoji = emoji,
                modifier = Modifier.fillMaxSize()
            )
        }"""

if old_code in content:
    content = content.replace(old_code, new_code)
    print("Replaced StickerCharacter Text with CharacterGraphic")
else:
    print("Could not find StickerCharacter Text")

with open("app/src/main/java/com/example/ui/components/CardboardComponents.kt", "w") as f:
    f.write(content)
