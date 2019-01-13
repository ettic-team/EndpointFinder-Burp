#!/bin/bash

mvn install:install-file -Dfile=lib/burp-extender-api-1.7.22.jar -DgroupId=net.portswigger.extender -DartifactId=burp-extender-api -Dversion=1.7.22 -Dpackaging=jar
mvn install:install-file -Dfile=lib/endpointfinder-java-0.0.1-SNAPSHOT.jar -DgroupId=endpointfinder-java -DartifactId=endpointfinder-java -Dversion=0.0.1 -Dpackaging=jar

mvn package
