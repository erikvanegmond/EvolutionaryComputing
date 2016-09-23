import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.vu.contest.ContestEvaluation;

class Population implements Iterator<Individual>{

    Individual[] population;
    private int populationSize;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private int evals = 0;
    private int index;


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
            Individual result = this.population[index];
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
        double maxFitness = -Integer.MIN_VALUE;
        reset();
        while(this.hasNext()){
            Individual individual = this.next();
            Double fitness = null;
            try {
                fitness = this.evaluateIndividual(individual);
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
        // Select parents
        Individual[] parents = getParents(2);
        // Apply crossover / mutation operators -> Create offspring
        Individual child = generateOffspring(parents);
        Arrays.sort(population);
        //overwriting the worst individual
        population[populationSize-1] = child;
        // Evaluate population
        evaluate();
        // Kill

    }

    private Individual[] getParents(int numParents){
        //simply get the best numParents individuals.
        Arrays.sort(population);
        return  Arrays.copyOfRange(population, 0, numParents);
    }

    private Individual generateOffspring(Individual[] parents){
        Individual child = new Individual(parents[0].getGenome());
        child.mutate();
        return child;
    }

}

