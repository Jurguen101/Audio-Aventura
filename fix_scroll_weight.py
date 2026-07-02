import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_code = """                        // Virtual Assistant
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {"""

new_code = """                        // Virtual Assistant
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
        f.write(content)
    print("Successfully fixed weight in scrollable column.")
else:
    print("Could not find the old code.")
