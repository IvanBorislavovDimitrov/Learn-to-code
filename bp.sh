#!/bin/bash
mvn clean install -DskipTests

cf push -f manifest.yml
