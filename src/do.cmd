javac -cp contest.jar *.java
jar cmf MainClass.txt submission.jar *.class
del *.class
java -jar testrun.jar -submission=player17 -evaluation=SphereEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=DeceptiveEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=RastriginEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=CrossInTrayEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=AckleyEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=GriewankEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=SchwefelEvaluation -seed=1
java -jar testrun.jar -submission=player17 -evaluation=LangermanEvaluation -seed=1
