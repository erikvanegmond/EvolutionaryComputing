import org.vu.contest.ContestEvaluation;

import java.util.*;

abstract class BasePopulation implements Iterator<Individual>{

    public Selector selector = new SelectTopN();

    Individual[] population;
    ContestEvaluation evaluation_;
    public int populationSize;
    public int genomeSize = 10;
    public int evaluations_limit_;

    public int evals = 0;
    public int index = 0;
    public boolean multimodal = false;
    public int noChangeCounter = 0;

    public double mutationRate;

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double best = -Double.MAX_VALUE;

    public int getEvals() {
        return evals;
    }

    public void setEvals(int evals) {
        this.evals = evals;
    }

    public BasePopulation(int populationSize, int evaluations_limit_, ContestEvaluation evaluation){
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new Individual(10);
        }
    }

    public int getNoChangeCounter() {
        return noChangeCounter;
    }

    public void setNoChangeCounter(int noChangeCounter) {
        this.noChangeCounter = noChangeCounter;
    }

    public boolean isMultimodal() {
        return multimodal;
    }

    public void setMultimodal(boolean multimodal) {
        this.multimodal = false;//multimodal;
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

    public void resetIndex() {
        this.index = 0;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public double evaluate() {
        double maxFitness = Integer.MIN_VALUE;
//        Individual bestIndividual = null;
        resetIndex();
        while (hasNext()) {
            Individual individual = next();
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

    public double evaluate(Individual[] individuals) {
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

    public double evaluateIndividual(Individual individual){
        if (!individual.hasScore()) {
            if (evals < evaluations_limit_) {
                evals++;
                double fitness = (double) evaluation_.evaluate(individual.getGenome());
                individual.setFitness(fitness);
            }
        }
        return individual.getFitness();
    }

    public boolean canEvaluate() {
        if (evals < evaluations_limit_) {
            return true;
        }
        return false;
    }

    public void sharedFitness() {
        final int sigma = 7; // sugested to be between 5 and 10. p93 Introduction to Evolutionary Computing
        final double alpha = 1;// linear shape for the sharing function.


        double[][] genomes = new double[populationSize][genomeSize];
        for(int i=0; i<populationSize; i++){
            genomes[i] = population[i].getGenome();
        }

        resetIndex();
        Individual individual;
        while (hasNext()) {
            individual = next();

            double sumShared = 0;
            for(int i=0; i<populationSize; i++){
                double[] other = genomes[i];
                double distance = individual.distance(other);
                if(distance <= sigma){
                    double shared = (1 - Math.pow(distance / sigma, alpha));
                    sumShared += shared;
                }
            }
            double sharedFitness = individual.getFitness()/sumShared;
            individual.setSharedFitness(sharedFitness);
        }
    }

    public Individual[] getParents(int numParents) {
//        Individual[] parents = tournamentParents(numParents);
        Individual[] parents = selector.select(numParents, population);
        return parents;
    }

    abstract void newGeneration();

}
