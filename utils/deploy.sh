#!/usr/bin/bash

#        DEPLOY SCRIPT FOR TOMCAT APIs      #
#              RUN USING SUDO               #

cd ../rest-sos
mvn clean package && echo "Build completed"
cp ./target/rest-sos-api.war /opt/tomcat/webapps/
sh /opt/tomcat/bin/startup.sh && echo "Tomcat running, completed successfuly"