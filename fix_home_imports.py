import re

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "r") as f:
    content = f.read()

# Clean up all compose material3 imports and Icons imports
content = re.sub(r'import androidx.compose.material3\..*\n', '', content)
content = re.sub(r'import androidx.compose.material.icons\..*\n', '', content)

new_imports = """import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
"""

# add them after CircleShape
content = content.replace("import androidx.compose.foundation.shape.CircleShape", "import androidx.compose.foundation.shape.CircleShape\n" + new_imports)

with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt", "w") as f:
    f.write(content)
