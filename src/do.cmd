javac -cp contest.jar *.java
jar cmf MainClass.txt submission.jar *.class
del *.class
java -jar testrun.jar -submission=player17 -evaluation=SphereEvaluation -seed=1
