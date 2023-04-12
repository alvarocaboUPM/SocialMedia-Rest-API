#!/usr/bin/bash

#        TOMCAT 9 INSTALLING PROCESS        #
#              RUN USING SUDO               #
#              by Álvaro Cabo               #

home=$(pwd)

if [ ! -d "/opt/tomcat" ]; then
  mkdir /opt/tomcat
fi

if ! getent group tomcat > /dev/null 2>&1; then
    groupadd tomcat 
fi

if ! getent passwd tomcat > /dev/null 2>&1; then
    useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
fi

if [ ! -d "/opt/tomcat" ]; then
    mkdir /opt/tomcat
    cd /tmp/
    if [ ! -f apache-tomcat-9.0.73.tar.gz ]; then 
        curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.73/bin/apache-tomcat-9.0.73.tar.gz; 
    fi
    tar xzvf apache-tomcat-*tar.gz -C /opt/tomcat --strip-components=1 && 
    echo -e "\e[36mMirror instalado correctamente\e[0m"
fi


echo -e "\e[36mCambiando permisos\e[0m"

cd /opt/tomcat
chgrp -R tomcat /opt/tomcat
chmod -R g+r /opt/tomcat/conf
chmod g+x /opt/tomcat/conf
chown -R tomcat /opt/tomcat/webapps/ /opt/tomcat/work/ /opt/tomcat/temp/ /opt/tomcat/logs/

sudo systemctl daemon-reload
cd $home
cp ./assets/tomcat-users.xml /opt/tomcat/conf && echo -e "\e[32mUsuarios habilitados\e[0m";

ufw allow 8080
sudo systemctl enable tomcat

if /opt/tomcat/bin/version.sh ; then echo -e "\e[32mInstalado correctamente\e[0m"; fi

sh deploy-v2.sh
