#!/usr/bin/bash

#        DEPLOY SCRIPT FOR TOMCAT APIs      #
#              RUN USING SUDO               #

cd ../rest-sos

mvn clean package && echo -e "\e[36mBuild completed!\e[0m"
cp -r ./target/rest-sos-1.1/META-INF ./src/main/WebInfo
cp -r ./target/rest-sos-1.1/WEB-INF ./src/main/WebInfo
cp ./target/rest-sos-api.war /opt/tomcat/webapps/ && echo -e "\e[36mWAR file copied\e[0m"
sh /opt/tomcat/bin/startup.sh && echo -e "\e[32mTomcat running, completed successfuly!\e[0m"