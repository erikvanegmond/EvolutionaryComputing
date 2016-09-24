import org.vu.contest.ContestEvaluation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

class Population implements Iterator<Individual>{

    Individual[] population;
    private int populationSize;
    ContestEvaluation evaluation_;
    private int evaluations_limit_;
    private int evals = 0;
    private int index = 0;
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
        // tournament selection: to select one individual, T (in this case 7) individuals are uniformly chosen, and the
        // best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
        Arrays.sort(population);
        return  Arrays.copyOfRange(population, 0, numParents);
        return  ;
    }

    private Individual[] tournament{
        // get 7 random indexes and compare the individuals. Take the best inidividual from the sample and call it parent 1
        // get 7 new indexes and use the ones that are not the same as the index of p1. Again, compare them and use the best
        // and call it parent2
        return
    }

    private Individual generateOffspring(Individual[] parents){
        //TODO add crossover

        //take the first parent and clone;
        Individual child = new Individual(parents[0].getGenome());

        child.mutate();
        return child;
    }

}

