#!/bin/sh
git pull
javac -cp contest.jar *.java
jar cmf MainClass.txt submission.jar *.class
java -jar testrun.jar -submission=player17 -evaluation=SphereEvaluation -seed=1
