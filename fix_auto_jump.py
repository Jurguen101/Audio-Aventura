import re

with open("app/src/main/java/com/example/ui/MainViewModel.kt", "r") as f:
    content = f.read()

old_code = """            if (chapterWasNotCompleted && previouslyCompletedCount == 9) {
                navigateTo(Screen.Credits)
            } else {
                navigateTo(Screen.Home)
            }"""

new_code = """            navigateTo(Screen.Home)"""

if old_code in content:
    content = content.replace(old_code, new_code)
    with open("app/src/main/java/com/example/ui/MainViewModel.kt", "w") as f:
        f.write(content)
    print("Removed automatic jump")
else:
    print("Could not find automatic jump")
