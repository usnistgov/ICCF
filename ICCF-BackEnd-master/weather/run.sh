#!/usr/bin/env bash

mvn clean install
java -jar target/clusteredWeatherLauncher.jar -cluster
