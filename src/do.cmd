javac -cp contest.jar *.java
jar cmf MainClass.txt submission.jar *.class
del player17.class
java -jar testrun.jar -submission=player17 -evaluation=LineEvaluation -seed=1