#!/usr/bin/env bash

mvn clean install
java -jar target/clusteredReceiverLauncher.jar -cluster
