with open("app/src/main/java/com/example/ui/screens/HomeScreen.kt") as f:
    lines = f.readlines()

depth = 0
for i, line in enumerate(lines):
    line_strip = line.strip()
    if not line_strip: continue
    
    open_count = line_strip.count('{')
    close_count = line_strip.count('}')
    
    if close_count > open_count:
        depth -= (close_count - open_count)
    
    print(f"{i+1:3d} | {'  '*depth}{line_strip}")
    
    if open_count > close_count:
        depth += (open_count - close_count)
