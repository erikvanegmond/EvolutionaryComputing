import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population implements Iterator<Individual>{

    Individual[] population;
    private int populationSize;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private int evals = 0;
    private int index = 0;
    private int tounamentSampleSize = 8;
    public String parentSelector = "best";


    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new Individual(10);
        }
    }

    @Override
    public boolean hasNext() {
        if(index < this.populationSize ){
            return true;
        }
        return false;
    }

    @Override
    public Individual next() {
        if(index < this.populationSize) {
            Individual result = population[index];
            index++;
            return result;
        }else{
            NoSuchElementException e = new NoSuchElementException("Element does not exist");
            throw e;
        }
    }

    @Override
    public void remove() {
        UnsupportedOperationException e = new UnsupportedOperationException();
        throw e;
    }

    public void reset() {
        this.index = 0;
    }

    public void evaluate() {
        double maxFitness = Integer.MIN_VALUE;
        Individual bestIndividual = null;
        reset();
        while(hasNext()){
            Individual individual = next();
            Double fitness = -Double.MAX_VALUE;
            try {
                fitness = evaluateIndividual(individual);
                if(fitness > maxFitness){
                    maxFitness = fitness;
                    bestIndividual = individual;
                }
            } catch (TooManyEvalsException e) {
                return;
            }
            individual.setFitness(fitness);
        }
        if(evals%100==0) {
            System.out.print(evals + " ");
            System.out.println(bestIndividual);
        }
    }

    public double evaluateIndividual(Individual individual) throws TooManyEvalsException {
        if(!individual.hasScore()) {
            if (evals < evaluations_limit_) {
                evals++;
                double fitness = (double) evaluation_.evaluate(individual.getGenome());
                individual.setFitness(fitness);
            } else {
                throw new TooManyEvalsException("You did too many evaluations");
            }
        }
        return individual.getFitness();
    }

    public boolean canEvaluate() {
        if(evals<evaluations_limit_){
            return true;
        }
        return false;
    }

    public void newGeneration() {
        //TODO Maybe more children from more couples
        // Select parents
        Individual[] parents = getParents(2);
        // Apply crossover / mutation operators -> Create offspring
        Individual child = generateOffspring(parents);

        //Replace the person who has lost in the tournament with the child
        int indexDying = tournamentDying();
        population[indexDying] = child;

        // Evaluate population
        evaluate();
        // Kill

    }

    private Individual[] getParents(int numParents){
        Individual[] parents = tournamentParents();
        return  parents;
    }

    private Individual[] tournamentParents(){
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
        double initialFitness = -Double.MAX_VALUE;

        // Find the index for the first parent
        List<Integer> populationRange = range(0, populationSize-1);
        List<Integer> sample1 = sample(populationRange);
        int parentIndex1 = selectIndividualForTournament(sample1, "best", initialFitness);

        // Remove first parent from list of possibilities
        Collections.sort(populationRange);
        populationRange.remove(parentIndex1);

        // Find the index for the second parent
        List<Integer> sample2 = sample(populationRange);
        int parentIndex2 = selectIndividualForTournament(sample2, "best", initialFitness);

        Individual[] parents = {population[parentIndex1], population[parentIndex2]};
        return parents;
    }

    private int tournamentDying(){
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)

        double initialFitness = Double.MAX_VALUE;

        // Find the index for the one who will be dying
        List<Integer> populationRange = range(0, populationSize-1);
        List<Integer> sample1 = sample(populationRange);
        int dyingIndex = selectIndividualForTournament(sample1, "worst", initialFitness);

        return dyingIndex;
    }

    private List<Integer> sample(List<Integer> listForSample){
        Collections.shuffle(listForSample);
        List<Integer> sample = listForSample.subList(0, tounamentSampleSize);
        return sample;
    }

    private int selectIndividualForTournament(List<Integer> indexSample, String tournamentType, double fitnessBestFit){
        int individualIndex = -1;
        for (int indexCounter = 0; indexCounter < tounamentSampleSize; indexCounter++) {
            int indexFromSample = indexSample.get(indexCounter);
            double individualFitness = population[indexFromSample].getFitness();
            if (tournamentType.equals("best")) {
                if (individualFitness > fitnessBestFit) {
                    fitnessBestFit = individualFitness;
                    individualIndex = indexFromSample;
                } else {
                    continue;
                }
            }
            else if (tournamentType.equals("worst")){
                if (individualFitness < fitnessBestFit) {
                    fitnessBestFit = individualFitness;
                    individualIndex = indexFromSample;
                } else {
                    continue;
                }
            }
            else{
                System.out.println("not a known tournamentType");
            }
        }
        if(evals%100==0) {
            System.out.println(population[individualIndex].getFitness());
        }
        return individualIndex;
    }

    public static List<Integer> range(int min, int max) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        return list;
    }

    private Individual generateOffspring(Individual[] parents){
        //Generate offspring based on uniform crossover

        if(parents != null) {
            Random rand = new Random();
            int n_parents = parents.length;
            int genome_lenght = parents[0].getGenome().length;
            double[] child_genome = new double[genome_lenght];
            for(int i=0; i<genome_lenght; i++){
                int random_parent = rand.nextInt(n_parents);
                child_genome[i] = parents[random_parent].getGenome()[i];
            }

            Individual child = new Individual(child_genome);

            child.mutate();
            return child;
        }else{
            return null;
        }
    }

}

