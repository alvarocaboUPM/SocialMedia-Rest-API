#!/usr/bin/bash

#        DEPLOY SCRIPT FOR TOMCAT APIs      #
#              RUN USING SUDO               #
#              by Álvaro Cabo               #

cd ../rest-sos

mvn clean package && echo -e "\e[36mBuild completed!\e[0m"
cp ./target/rest-sos-api.war /opt/tomcat/webapps/ && echo -e "\e[36mWAR file copied\e[0m"
sh /opt/tomcat/bin/shutdown.sh
sh /opt/tomcat/bin/startup.sh && echo -e "\e[32mTomcat running, completed successfuly!\e[0m"