import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

old_trigger = """                                CreditsTriggerPage(
                                    allCompleted = allCompleted,
                                    onTriggerClick = { viewModel.navigateTo(Screen.Credits) }
                                )"""
new_trigger = """                                CreditsTriggerPage(
                                    allCompleted = allCompleted,
                                    onTriggerClick = { viewModel.navigateTo(Screen.FinalCredits) }
                                )"""

if old_trigger in content:
    content = content.replace(old_trigger, new_trigger)
    print("Updated trigger.")
else:
    print("Could not find trigger.")

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
