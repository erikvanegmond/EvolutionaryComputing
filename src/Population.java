import org.vu.contest.ContestEvaluation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

class Population implements Iterator<Individual>{

    Individual[] population;
    private int populationSize;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private int evals = 0;
    private int index = 0;
    private int tounamentSampleSize = 7;
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
        reset();
        while(hasNext()){
            Individual individual = next();
            Double fitness = -Double.MAX_VALUE;
            try {
                fitness = evaluateIndividual(individual);
                if(fitness > maxFitness){
                    maxFitness = fitness;
                }
            } catch (TooManyEvalsException e) {
                return;
            }
            individual.setFitness(fitness);
        }
        System.out.println(maxFitness);
    }

    public double evaluateIndividual(Individual individual) throws TooManyEvalsException {
        if(!individual.hasScore()) {
            if (evals < evaluations_limit_) {
                evals++;
                return (double) evaluation_.evaluate(individual.getGenome());
            } else {
                throw new TooManyEvalsException("You did too many evaluations");
            }
        }else{
            return individual.getFitness();
        }
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

        //TODO compbine old populations with new offspring
        Arrays.sort(population);
        //overwriting the worst individual
        population[populationSize-1] = child;

        // Evaluate population
        evaluate();
        // Kill

    }

    private Individual[] getParents(int numParents){
        Individual[] parents = tournament();
        return  parents;
    }

    private Individual[] tournament(){
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
        List<Integer> populationRange = range(0, populationSize-1);
        Collections.shuffle(populationRange);
        List<Integer> sample1 = populationRange.subList(0, tounamentSampleSize);
        Collections.shuffle(populationRange);
        List<Integer> sample2 = populationRange.subList(0, tounamentSampleSize);
        int parentIndex1 = selectBestParent(sample1);
        int parentIndex2 = selectBestParent(sample2);
        Individual[] parents = {population[parentIndex1], population[parentIndex2]};
        return parents;
    }

    private int selectBestParent(List<Integer> indexSample){
        int bestIndividualIndex = -1;
        for (int indexCounter = 0; indexCounter < tounamentSampleSize; indexCounter++) {
            double bestFitness = -Double.MAX_VALUE;
            int indexFromSample = indexSample.get(indexCounter);
            double individualFitness = population[indexFromSample].getFitness();
            if (individualFitness > bestFitness){
                bestFitness = individualFitness;
                bestIndividualIndex = indexCounter;
            }
            else{
                continue;
            }
        }
        return bestIndividualIndex;
    }

    public static List<Integer> range(int min, int max) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        return list;
    }

    private Individual generateOffspring(Individual[] parents){
        //TODO add crossover

        //take the first parent and clone;
        Individual child = new Individual(parents[0].getGenome());

        child.mutate();
        return child;
    }

}

