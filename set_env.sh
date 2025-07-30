#!/bin/bash

# Load from config
source config/config.properties

# Merge inputs and config
BROWSER="${INPUT_BROWSER:-$browser}"
XMLFILE="${INPUT_XMLFILE:-$xmlfile}"
RUNMODE="${INPUT_RUNMODE:-$runmode}"
PLATFORM="${INPUT_PLATFORM:-$platform}"

# Export to GitHub env
echo "BROWSER=$BROWSER" >> $GITHUB_ENV
echo "XMLFILE=$XMLFILE" >> $GITHUB_ENV
echo "RUNMODE=$RUNMODE" >> $GITHUB_ENV
echo "PLATFORM=$PLATFORM" >> $GITHUB_ENV
