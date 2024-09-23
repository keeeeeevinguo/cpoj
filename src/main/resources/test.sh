#! /bin/bash

TEST_FOLDER="${1:-"data"}"
# COMMAND, must be 'judge-compile' or 'judge-execute'
COMMAND="${2:-"NA"}"
# Language, i.e., Java
LANGUAGE="${3:-"NA"}"
# Code file, HelloWorld.java
CODE_FILE="${4:-"NA"}"
# Input test case file, input15.txt
INPUT_FILE="${5:-"NA"}"
# Timeout in seconds
TIMEOUT="${6:-"5"}"

echo "TEST_FOLDER: ${TEST_FOLDER}" 1>&2
echo "COMMAND: ${COMMAND}"
echo "LANGUAGE: ${LANGUAGE}"
echo "CODE_FILE: ${CODE_FILE}"
echo "INPUT_FILE: ${INPUT_FILE}"
echo "TIMEOUT: ${TIMEOUT} seconds"

pwd

ls
