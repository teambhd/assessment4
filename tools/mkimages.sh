#!/usr/bin/env sh

# Ensure that the text_graphics directory exists
mkdir -p ../src/res/text_graphics

# The loading text for the splash screen
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:"loading..." \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/loading.png


# The title text
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 120 \
label:"don't crash" \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/title.png


# The Help button
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'help' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/help.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'help' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/help_hover.png


# The Quit button
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'quit' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/quit.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'quit' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/quit_hover.png


# The Challenge Mode button
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 56 \
label:'challenge mode' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/challenge.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 56 \
label:'challenge mode' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/challenge_hover.png


# The Versus Mode button
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 56 \
label:'versus mode' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/versus.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 56 \
label:'versus mode' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/versus_hover.png

# The button for the Easy difficulty mode
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'easy' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/easy.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'easy' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/easy_hover.png

# The button for the Medium difficulty mode
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'medium' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/medium.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'medium' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/medium_hover.png

# The button for the Hard difficulty mode
montage -background none \
-fill white \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'hard' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/hard.png

montage -background none \
-fill "#E2CD7D" \
-font ../src/res/fonts/a-love-of-thunder.ttf  \
-pointsize 48 \
label:'hard' \
+set label -shadow \
-background none \
-geometry +0+0 \
-define png:bit-depth=8 \
../src/res/text_graphics/hard_hover.png


