import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_code = """                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {"""

new_code = """                    import androidx.compose.foundation.rememberScrollState
                    import androidx.compose.foundation.verticalScroll
                    
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {"""

if old_code in content:
    # Wait, we can't put imports inside a function in Kotlin like that. 
    # I should just add the modifier.
    pass
