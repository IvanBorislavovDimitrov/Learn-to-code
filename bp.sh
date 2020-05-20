#!/bin/bash
mvn clean install -DskipTests
cf d learn-to-code-backend -f
#cf ds learn-to-code-database -f
#cf cs postgresql v9.6-dev-large learn-to-code-database
cf push -f manifest.yml
