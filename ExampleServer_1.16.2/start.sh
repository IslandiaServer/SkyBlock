#!/bin/sh
screen -S skyblock -X quit
screen -S skyblock -m -d java -Xincgc -server -jar -Xms3000m -Xmx3000m spigot-1.16.2.jar
