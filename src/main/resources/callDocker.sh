#! /bin/bash

# ---------------------------------------------------------------------------------------
# VARIABLE DECLARATIONS
# ---------------------------------------------------------------------------------------

# Time out: timeout --kill-after=30s 30s docker stop
# Memory: -m 512M --memory-swap 512M
# CPU: no way to control absolute cpu use limit, should number of dockers < number of cpu cores
# Disck usage

#docker build -t taylor/sageroj:v2 .
#docker run -it -v /Users/taylor/Development/WebWorkspace/MEAN/SagerOJ/sageroj/judge_docker/runJudgeDocker/testData/java:/judge/data taylor/sageroj:v2 bash

#bash judge.sh judge-compile Java HelloJavaGood.java
#bash judge.sh judge-execute Java HelloJavaGood.class input1.txt

#bash judge.sh judge-compile C helloC.c
#bash judge.sh judge-execute C helloC input1.txt

#bash judge.sh judge-compile C++ helloCPP.cpp
#bash judge.sh judge-execute C++ helloCPP input1.txt

#bash judge.sh judge-compile python2 helloPython2.py
#bash judge.sh judge-execute python2 helloPython2.py input1.txt

#bash judge.sh judge-compile python3 helloPython3.py
#bash judge.sh judge-execute python3 helloPython3.py input1.txt

#bash callDocker.sh data judge-compile java HelloJavaGood.java input1.txt 10
#bash callDocker.sh data judge-execute java HelloJavaGood.class input1.txt 10

#docker run --rm -it -m 512M -v $(pwd)/data:/judge/data --entrypoint /bin/bash cagewyt/onlinejudge:latest

# Test folder containing code file and input file

TEST_FOLDER="${1:-"data"}"
DOCKER_CONTAINER_NAME="${2:-"NA"}"
# COMMAND, must be 'judge-compile' or 'judge-execute'
COMMAND="${3:-"NA"}"
# Language, i.e., Java
LANGUAGE="${4:-"NA"}"
# Code file, HelloWorld.java
CODE_FILE="${5:-"NA"}"
# Input test case file, input15.txt
INPUT_FILE="${6:-"NA"}"
# Timeout in seconds
TIMEOUT="${7:-"5"}"


SUCCESS="0"
UNSUPPORTED_COMMAND="40"
CODE_FILE_NOT_FOUND="42"
INPUT_FILE_NOT_FOUND="43"
TEST_FOLDER_NOT_FOUND="45"

TIMEOUT_ERROR_RUN_DOCKER="33"
TIMEOUT_ERROR_STOP_DOCKER="34"
TIMEOUT_ERROR_REMOVE_DOCKER="35"
TIMEOUT_CODE="36"

JUDGE_ERROR="60"

RETURN_CODE="0"
RETURN_MSG="SUCCESS"

echo "TEST_FOLDER: ${TEST_FOLDER}"
echo "DOCKER_CONTAINER_NAME: ${DOCKER_CONTAINER_NAME}"
echo "COMMAND: ${COMMAND}"
echo "LANGUAGE: ${LANGUAGE}"
echo "CODE_FILE: ${CODE_FILE}"
echo "INPUT_FILE: ${INPUT_FILE}"
echo "TIMEOUT: ${TIMEOUT} seconds"

# ---------------------------------------------------------------------------------------
# FUNCTION DECLARATIONS
# ---------------------------------------------------------------------------------------

function failIfNonZeroExitCode()
{ 
  COMMAND_STATUS=$?
    if [ "${COMMAND_STATUS}" -ne 0 ]; then
      echo " ****** FAILED TO RUN COMMAND: aborting init ****** "
      exit -1
    fi
}

function isTimeout()
{ 
  COMMAND_STATUS=$?
  if [ "${COMMAND_STATUS}" -eq 124 ] || [ "${COMMAND_STATUS}" -eq 137 ]; then
    return ${TIMEOUT_CODE}
  fi
  return ${SUCCESS}
}

function cleanupDocker()
{
  echo "INFO: Clean up docker containers..."
  # To stop all docker containers:
  #timeout --kill-after=30s 30s docker stop $(docker ps -a -q)
#  docker stop $(docker ps -a -q) > /dev/null 2>&1
#  isTimeout
#  COMMAND_STATUS=$?
#  if [ "${COMMAND_STATUS}" -eq ${TIMEOUT_CODE} ]; then
#    echo "TIMEOUT_ERROR_STOP_DOCKER" 1>&2
#    RETURN_CODE=${TIMEOUT_ERROR_STOP_DOCKER}
#  fi
#
#  # Then to remove all docker containers:
#  #timeout --kill-after=30s 30s docker rm -f $(docker ps -a -q)
#  docker rm -f $(docker ps -a -q) > /dev/null 2>&1
#  isTimeout
#  COMMAND_STATUS=$?
#  if [ "${COMMAND_STATUS}" -eq ${TIMEOUT_CODE} ]; then
#    echo "TIMEOUT_ERROR_REMOVE_DOCKER" 1>&2
#    RETURN_CODE=${TIMEOUT_ERROR_REMOVE_DOCKER}
#  fi
  docker stop ${DOCKER_CONTAINER_NAME} > /dev/null 2>&1
  docker rm -f ${DOCKER_CONTAINER_NAME} > /dev/null 2>&1

  # Delete output file if empty
  if [[ ! -s $(pwd)/data/output/stdout.txt ]] ; then
    rm -f $(pwd)/data/output/stdout.txt > /dev/null 2>&1
  fi;

  if [[ ! -s $(pwd)/data/output/stderr.txt ]] ; then
    rm -f $(pwd)/data/output/stderr.txt > /dev/null 2>&1
  fi;

  if [[ ! -s $(pwd)/data/output/timeout.txt ]] ; then
    rm -f $(pwd)/data/output/timeout.txt > /dev/null 2>&1
  fi;

  if [[ ! -s $(pwd)/data/output/log.txt ]] ; then
    rm -f $(pwd)/data/output/log.txt > /dev/null 2>&1
  fi;

  if [[ ! -s $(pwd)/data/output/exit_code.txt ]] ; then
    rm -f $(pwd)/data/output/exit_code.txt > /dev/null 2>&1
  fi;
}

function sanityCheck()
{
  echo "Begin sanityCheck"
  if [ ! -d "$TEST_FOLDER" ]; then 
    echo "ERROR: Folder $TEST_FOLDER does not exist"

    RETURN_CODE=${TEST_FOLDER_NOT_FOUND}
    echo "TEST_FOLDER_NOT_FOUND" 1>&2
    exit ${RETURN_CODE}
  fi

  pushd $TEST_FOLDER > /dev/null 2>&1
  
  if [ ! -f "$CODE_FILE" ]; then
    echo "ERROR: File $CODE_FILE does not exists"

    RETURN_CODE=${CODE_FILE_NOT_FOUND}
    echo "CODE_FILE_NOT_FOUND" 1>&2
    exit ${RETURN_CODE}
  fi

  if [ "${COMMAND,,}" == "judge-execute" ]; then
    if [ ! -f "$INPUT_FILE" ]; then
      echo "ERROR: File $INPUT_FILE does not exists"

      RETURN_CODE=${INPUT_FILE_NOT_FOUND}
      echo "INPUT_FILE_NOT_FOUND" 1>&2
      exit ${RETURN_CODE}
    fi
  fi

  # Force delete output folder if exist
  if [ -d "output" ]; then
    rm -Rf output;
  fi

  popd > /dev/null 2>&1
}

function judgeExecute()
{
  echo "INFO: Call judge execute"
  #timeout --kill-after=${TIMEOUT}s ${TIMEOUT}s docker run -it -v $(pwd)/data:/judge/data taylor/sageroj:v2 bash
  docker run --platform linux/amd64 --rm -t \
    -m 512M --memory-swap 512M \
    -v $(pwd)/data:/judge/data \
    -e ojCOMMAND=${COMMAND} \
    -e ojLANGUAGE=${LANGUAGE} \
    -e ojCODE_FILE=${CODE_FILE} \
    -e ojINPUT_FILE=${INPUT_FILE} \
    -e ojTIMEOUT=${TIMEOUT} \
    --net=none \
    --name ${DOCKER_CONTAINER_NAME} \
    cagewyt/onlinejudge:latest

#    --cidfile="dockerid.txt" \

  isTimeout 

  COMMAND_STATUS=$?
  if [ "${COMMAND_STATUS}" -eq ${TIMEOUT_CODE} ]; then
    RETURN_CODE=${TIMEOUT_ERROR_RUN_DOCKER}
    echo "TIMEOUT_ERROR_RUN_DOCKER" 1>&2
  fi
}

function judgeCompile()
{
  echo "INFO: Call judge compile"
  #timeout --kill-after=${TIMEOUT}s ${TIMEOUT}s docker run -it -v $(pwd)/data:/judge/data taylor/sageroj:v2 bash
  #docker run --rm -t -m 512M -v $(pwd)/data:/judge/data taylor/sageroj:v4
  #docker run --rm -it -m 512M -v $(pwd)/data:/judge/data --entrypoint /bin/bash taylor/sageroj:v4 
  docker run --platform linux/amd64 --rm -t \
    -m 512M --memory-swap 512M \
    -v $(pwd)/data:/judge/data \
    -e ojCOMMAND=${COMMAND} \
    -e ojLANGUAGE=${LANGUAGE} \
    -e ojCODE_FILE=${CODE_FILE} \
    -e ojINPUT_FILE=${INPUT_FILE} \
    -e ojTIMEOUT=${TIMEOUT} \
    --net=none \
    --name ${DOCKER_CONTAINER_NAME} \
    cagewyt/onlinejudge:latest

  isTimeout 

  COMMAND_STATUS=$?
  if [ "${COMMAND_STATUS}" -eq ${TIMEOUT_CODE} ]; then
    RETURN_CODE=${TIMEOUT_ERROR_RUN_DOCKER}
    echo "TIMEOUT_ERROR_RUN_DOCKER" 1>&2
  fi
}

function judge()
{
  echo "Begin judge"
  sanityCheck

  if [ "${COMMAND,,}" == "judge-compile" ]; then
    judgeCompile
  elif [ "${COMMAND,,}" == "judge-execute" ]; then
    judgeExecute
  else 
    echo "Unsupported command: ${COMMAND}"
    RETURN_CODE=${UNSUPPORTED_COMMAND}
    echo "UNSUPPORTED_COMMAND" 1>&2
    exit ${RETURN_CODE}
  fi
  
  if [ "${RETURN_CODE}" -eq ${SUCCESS} ]; then
    echo "INFO: Judge ran successfully - $(date)" 
    echo "SUCCESS"
  fi
  return ${RETURN_CODE}
}

# ---------------------------------------------------------------------------------------
# SCRIPT EXECUTION
# ---------------------------------------------------------------------------------------
echo "========================================================================================="
{ # your 'try' block
    judge
} || { # your 'catch' block
    echo "JUDGE_ERROR $?" 1>&2 
}
cleanupDocker # finally: this will always happen
exit ${RETURN_CODE}
