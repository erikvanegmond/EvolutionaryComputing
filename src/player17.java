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


    public player17()
    {
        rnd_ = new Random();
    }

    public void setSeed(long seed)
    {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation)
    {
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
//        Population pop =  new Population(population_limit, evaluations_limit_, evaluation_);;
//        pop.evaluate();
//
//        while(pop.canEvaluate()){
//            pop.newGeneration();
//        }

        // init population
        Population pop = new Population(population_limit, evaluations_limit_, evaluation_);


        //evaluate entire population
        pop.evaluate();
        if(isMultimodal){
            pop.setMultimodal(isMultimodal);
            pop.sharedFitness();
        }

        int c = 0;
        while(pop.canEvaluate()){
            System.out.println(c);
            if(pop.getNoChangeCounter() > 10){
                pop.setMutationRate(pop.getMutationRate()*1.01);
                pop.setNoChangeCounter(9);
            }
            if(pop.getNoChangeCounter() < 2){
                pop.setMutationRate(1);
            }
            pop.newGeneration();
            c++;
        }
    }



}


