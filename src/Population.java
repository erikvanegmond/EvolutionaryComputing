import org.vu.contest.ContestEvaluation;

class Population extends BasePopulation {

    private int tounamentSampleSize = 18;
    private Crossover crossover = new UniformCrossover();
    private ListCrossover listCrossover = new AllWithAllCrossover();
    private Selector selector = new SelectTopN();
    private Mutator mutator = new NonUniformMutation();

    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        super(populationSize, evaluations_limit_, evaluation);
        mutationRate = 0.1;
    }

    public void newGeneration() {
        /*  1) Make parents list
            2) Mate parents -> children
            3) Mutate children -> sadChildren?
            4) Evaluate
            5) Select topn -> new pop
        */

        // 1) Make parents list
        Individual[] parents = getParents(10);

        // 2) mate parents
        Individual[] children = listCrossover.combinelist(parents, crossover);

        // 3) mutate children
        for(int i=0; i<children.length; i++){
            mutator.mutate(children[i]);
        }

        // 4.5) combine
        Individual[] combined = (Individual[]) Utils.mergeIndividualLists(population, children);

        // 4) Evaluate entire children
        double best = evaluate(combined);

        // 5) select top n to make new population
        population = selector.select(populationSize, combined);
        System.out.println("Best:"+best+" average: "+averageFitness());

    }


    private double averageFitness(){
        double sum = 0;
        int popSize = population.length;
        for(Individual individual : population){
            sum += individual.getFitness();
        }
        return sum/popSize;
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