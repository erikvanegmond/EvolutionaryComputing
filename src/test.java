import org.vu.contest.ContestEvaluation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikv on 14-12-2016.
 */
public class test {

    public static void main(String[] args) throws ClassNotFoundException {
        int numTests = 10;

        List<List<ContestEvaluation>> listOvEvaluators = new ArrayList<>();
        List<ContestEvaluation> evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new SphereEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new RastriginEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new FletcherPowellEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new DeceptiveEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new CrossInTrayEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new AckleyEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new GriewankEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new SchwefelEvaluation());
        }
        listOvEvaluators.add(evaluators);
        evaluators = new ArrayList<>();
        for(int i=0; i<numTests; i++) {
            evaluators.add(new LangermanEvaluation());
        }
        listOvEvaluators.add(evaluators);

        runTest(listOvEvaluators, numTests);

    }

    public static void runTest(List<List<ContestEvaluation>> listOvEvaluators, int numTests){

        for(List<ContestEvaluation> evaluators:listOvEvaluators) {
            StringBuilder string = new StringBuilder();
            string.append(evaluators.get(0).getClass());
            System.out.println(string.toString());
            for (ContestEvaluation eval : evaluators) {
                player17 sub = new player17();
                sub.setEvaluation(eval);
                sub.run();
                string.append(",");
                string.append(sub.getScore());
            }
            string.append("\n");
            writeToFile(string.toString());
        }
    }

    public static void writeToFile(String line){
        try {
            Files.write(Paths.get("file.txt"), line.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
            //exception handling left as an exercise for the reader
        }
    }
}
