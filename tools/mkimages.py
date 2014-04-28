#!/usr/bin/env python

from os import system

# Define our functions
def create_image(text, font_size=48, filename=None, color="white"):
    if filename == None:
        filename = text 
    command  = "montage" 
    command += " -background none"
    command += " -fill \"" + color + "\""
    command += " -font ../src/res/fonts/a-love-of-thunder.ttf"
    command += " -pointsize " + str(font_size)
    command += " label:\"" + text + "\""
    command += " +set label -shadow"
    command += " -background none"
    command += " -geometry +0+0"
    command += " -define png:bit-depth=8"
    command += " ../src/res/text_graphics/" + filename + ".png"
    system(command)
   
def create_hover_image(text, font_size=48, filename=None):
    if filename == None:
        filename = text 
    create_image(text, font_size, filename, "white")
    create_image(text, font_size, filename + "_hover", "#E2CD7D")

    
# Ensure that the text_graphics directory exists
system("mkdir -p ../src/res/text_graphics")

# The loading text for the splash screen
create_image("loading...", 48, "loading")

# The main Don't Crash title text
create_image("don't crash", 120, "title")

# Titles for other screens
create_image("game paused", 72, "paused")
create_image("game over", 72, "gameover")

# Create all our standard buttons
create_hover_image("help")
create_hover_image("quit")
create_hover_image("menu")

# Buttons for difficulty selection
create_hover_image("easy")
create_hover_image("medium")
create_hover_image("hard")

# Slightly larger buttons for the "play" buttons
create_hover_image("challenge mode", 56, "challenge")
create_hover_image("versus mode", 56, "versus")
create_hover_image("play again", 56, "again")
create_hover_image("resume", 56)
    