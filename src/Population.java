import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population extends BasePopulation {

    private int tounamentSampleSize = 18;
    private Crossover crossover = new RandomBlendCrossover();
    private ListCrossover listCrossover = new AllWithAllCrossover();
    private Selector selector = new SelectTopN();
    private Mutator mutator = new NonUniformMutation();

    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        super(populationSize, evaluations_limit_, evaluation);
        mutationRate = 0.01;
    }

    public void newGeneration() {
        /*  1) Make parents list
            2) Mate parents -> children
            3) Mutate children -> sadChildren?
            4) Evaluate
            5) Select topn -> new pop
        */

        // 1) Make parents list
        Individual[] parents = getParents(20);

        // 2) mate parents
        Individual[] children = listCrossover.combinelist(parents, crossover);
        System.out.println("n children: "+children.length);

        // 3) mutate children
        for(int i=0; i<children.length; i++){
            mutator.mutate(children[i]);
        }

        // 4) Evaluate entire children
        evaluate(children);

        // 5) select top n to make new population
        if( selector.select(populationSize, children)!=null) {
            population = selector.select(populationSize, children);
        }else{
            System.out.println();
        }

    }


    private double averageFitness(){
        double sum = 0;
        populationSize = population.length;
        for(Individual individual : population){
            sum += individual.getFitness();
        }
        return sum/populationSize;
    }

    private Individual[] generateOffspring(Individual[] parents) {
        //Generate offspring based on uniform crossover or blend crossover
        // combines all parents with each other
        int nParents = parents.length;
        int n = 0;

        ListCrossover listCrossover = new AllWithAllCrossover();

        Individual[] children = listCrossover.combinelist(parents, crossover);

        for(int i=0; i<children.length; i++){
            mutator.mutate(children[i]);
        }

        return children;
    }

}