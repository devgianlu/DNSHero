#!/bin/bash
set -ev

if [[ -z "${TRAVIS_TAG}" ]]; then
    ./gradlew assembleRelease publish
else
    ./gradlew build connectedCheck
fi