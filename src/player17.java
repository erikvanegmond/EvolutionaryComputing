import org.vu.contest.ContestEvaluation;
import org.vu.contest.ContestSubmission;

import java.util.Properties;
import java.util.Random;

//From player 20 setup
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class player17 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    final private int population_limit = 200;
    final private int individual_size = 10;
    private boolean isMultimodal;
    private boolean hasStructure;
    private boolean isSeparable;
    private int differential = 0;
    ArrayList<Individual[]>  individuals;
    private Crossover crossover = new RandomBlendCrossover();
    static NumberFormat formatter3 = new DecimalFormat("#00000");
    private double best = -Double.MAX_VALUE;
    private double min = -50;
    private double max = 50;
    private int populationSize = 100;
    private int evals = 0;


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

        //All things that needed to be initialized
        individuals = new ArrayList<Individual[]>();
        ListCrossover listCrossover = new AllWithAllCrossover();
        int c = 0;
        int numberOfChildren = 100;
        Individual[] parents;
        Individual[] happyChildren;
        Individual[] sadChildren = new Individual[200];
        int noChangeCounter = 0;
        BasePopulation pop;

        // init population
        switch (differential) {
            case 0: {
                pop = new Population(population_limit, evaluations_limit_, evaluation_);
                break;
            }
            case 1: {
                pop = new DiffPopulation(population_limit, evaluations_limit_, evaluation_);
                break;
            }
            default: {
                pop = new Population(population_limit, evaluations_limit_, evaluation_);
                break;
            }
        }

        //evaluate entire population
        pop.evaluate();
        Individual[] population = pop.population;
        System.out.println("population length: " + population.length);

        while (evals < evaluations_limit_) {

            // select parents
            parents = getParents(50, population);
//            Arrays.fill(sadChildren, null);
//            System.out.println("sadChildren: " + sadChildren);
            //creates an empty array of length population_limit
//            Arrays.fill(population, null);

            // have some children
            happyChildren = listCrossover.combinelist(parents, crossover);
//            Arrays.fill(parents, null);

            // mutate the offspring
            sadChildren = new Individual[happyChildren.length];
            for (int i = 0; i < happyChildren.length; i++) {
                sadChildren[i] = nonuniformMutation(0.005, happyChildren[i]);
            }

//            Arrays.fill(happyChildren, null);

            // check fitness
            evaluate(sadChildren);

            // select survivors
            //TO DO: make method which passes the population (sadChildren) not using the pop object
            population = selectTopN(numberOfChildren, sadChildren);

            double best = evaluate(population);
            if (best > this.best) {
                this.best = best;
                noChangeCounter = 0;
            } else {
                noChangeCounter++;
            }
            System.out.println("best and average: " + best +" "+ averageFitness(population));

            c++;
        }

//        System.out.println("number of combine population: " + sadChildren.length());
//        System.out.println("Evals: " + formatter3.format(evals));
//
//        System.out.println("c: " + c);
//        Collections.sort(population, Collections.reverseOrder());
//        System.out.println("Done. \nTop 5:");
//        for (int i = 0; i < 5; i++) {
//            System.out.println(population[i].toString());

            //        if(isMultimodal && differential == 0){
//            pop.setMultimodal(isMultimodal);
//            pop.sharedFitness();
//        }
//
//        while(pop.canEvaluate()){
//            System.out.println("hello");
//            if(pop.getNoChangeCounter() > 10 && differential == 0){
//                pop.setMutationRate(pop.getMutationRate()*1.01);
//                pop.setNoChangeCounter(9);
//            }
//            if(pop.getNoChangeCounter() < 2 && differential == 0){
//                pop.setMutationRate(1);
//            }
//            if(differential == 0) {
//                pop.newGeneration();
//            }
//            else{
//                pop.newGeneration();
//            }
//        }
//        }
    }

    private Individual[] getParents(int numParents, Individual[] population) {
//          Individual[] parents = tournamentParents(numParents);
        Individual[] parents = selectTopN(numParents, population);
        return parents;
    }


    private Individual[] selectTopN(int n, Individual[] selectFrom) {
        if (n < selectFrom.length) {
            Arrays.sort(selectFrom);
            Individual[] selected = Arrays.copyOfRange(selectFrom, 0, n);
            return selected;
        } else {
            return null;
        }
    }


    private Individual nonuniformMutation(double sigma, Individual child) {
        final double mutationChance = 1;
        Random rand = new Random();
        double[] genome = child.getGenome();
        for(int i=0; i < genome.length; i++){
            if (rand.nextDouble() < mutationChance){
                genome[i] += rand.nextGaussian()*sigma;
                //Stay within the search range.
                if(genome[i] < min){
                    genome[i] = min;
                }else if(genome[i] > max){
                    genome[i] = max;
                }

            }
        }
        return new Individual(genome);
    }

    private double evaluate(Individual[] individuals) {
        double maxFitness = Integer.MIN_VALUE;

        for(Individual individual : individuals){
            Double fitness = -Double.MAX_VALUE;
            fitness = evaluateIndividual(individual);
            if (fitness > maxFitness) {
                maxFitness = fitness;
//                bestIndividual = individual;
            }
            individual.setFitness(fitness);
        }
        return maxFitness;
    }

    private double averageFitness(Individual[] population){
        double sum = 0;
        populationSize = population.length;
        for(Individual individual : population){
            sum += individual.getFitness();
        }
        System.out.println("sum/population: " +  sum/populationSize);
        return sum/populationSize;
    }

    public double evaluateIndividual(Individual individual){
        if (!individual.hasScore()) {
            if (evals < evaluations_limit_) {
                evals++;
                double fitness = -Double.MAX_VALUE;
                if (evaluation_.evaluate(individual.getGenome()) != null) {
                    System.out.println("Nullpointer: " + (double) evaluation_.evaluate(individual.getGenome()));
                    fitness = (double) evaluation_.evaluate(individual.getGenome());
                }
                individual.setFitness(fitness);
            }
        }
        return individual.getFitness();
    }

}


