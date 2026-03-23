#!/bin/bash

set -e

# Variables
INFO='\033[1;33m'
SUCCESS='\033[1;32m'
NC='\033[0m' # No Color (Reset)

echo -e "${INFO}--- Miztli App Builder ---${NC}"


echo -e "${INFO}Step one: Build the main app (All AWS Lambda code...)${NC}"
cd backend-spring/
./gradlew spotlessApply
./gradlew clean build


echo -e "${INFO}Step two: Build CDK Infrastructure code...${NC}"
cd ../infra-cdk
./gradlew spotlessApply
./gradlew clean build


echo -e "${SUCCESS}--- All set! ---${NC}"
