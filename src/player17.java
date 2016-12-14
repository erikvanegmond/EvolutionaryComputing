import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.Properties;
import java.util.Random;

//From player 20 setup

public class player17 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    final private int population_limit = 150;
    private boolean isMultimodal;
    private boolean hasStructure;
    private boolean isSeparable;

    private double score;

    public double getScore(){
        return score;
    }

    public player17()
    {
        rnd_ = new Random();
    }

    public void setSeed(long seed){
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation){
        // Set evaluation problem used in the run
        evaluation_ = evaluation;

        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));


    }

    public void run() {
        // init population
        int initPopulationSize = (int) Math.floor( (evaluations_limit_*0.1) );
        Population pop = new Population(initPopulationSize, evaluations_limit_, evaluation_);
        //evaluate entire population
        pop.evaluate();
        pop.changePopulationLimit(population_limit);
        if(isMultimodal){
            pop.setMultimodal(isMultimodal);
        }

        while(pop.canEvaluate()){
            pop.newGeneration();
        }
        score = evaluation_.getFinalResult();

    }



}


