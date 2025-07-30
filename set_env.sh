#!/bin/bash

# Set the path to the config file using relative path
CONFIG_FILE="src/main/java/config/config.properties"

# Load from config file if it exists
if [ -f "$CONFIG_FILE" ]; then
  source <(grep = "$CONFIG_FILE" | sed 's/\r//')
else
  echo "❌ Config file not found at $CONFIG_FILE"
  exit 1
fi

# Merge GitHub Action inputs with config values as fallback
BROWSER="${INPUT_BROWSER:-$browser}"
XMLFILE="${INPUT_XMLFILE:-testng.xml}"  # fallback if not in config
RUNMODE="${INPUT_RUNMODE:-$runmode}"
PLATFORM="${INPUT_PLATFORM:-$platform}"

# Export variables to GitHub Actions environment
echo "BROWSER=$BROWSER" >> $GITHUB_ENV
echo "XMLFILE=$XMLFILE" >> $GITHUB_ENV
echo "RUNMODE=$RUNMODE" >> $GITHUB_ENV
echo "PLATFORM=$PLATFORM" >> $GITHUB_ENV
