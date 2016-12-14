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
        player17 sub = new player17();
        int numTests = 10;

        List<ContestEvaluation> evaluators = new ArrayList<ContestEvaluation>();
        evaluators.add(new SphereEvaluation());
        runTest(evaluators, numTests, sub);

    }

    public static void runTest(List<ContestEvaluation> evaluators, int numTests, player17 sub){
        for (ContestEvaluation eval:evaluators) {
            StringBuilder string = new StringBuilder();
            string.append("shpere");
            for(int i=0; i<numTests; i++){
                evaluators.get(0).getClass();
                sub.setEvaluation(new SphereEvaluation());
                sub.run();
                System.out.println(sub.getScore());
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
