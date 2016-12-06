import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population implements Iterator<Individual> {

    Individual[] population;
    ContestEvaluation evaluation_;
    private int populationSize;
    private int genomeSize = 10;
    private int evaluations_limit_;
    private double mutationRate = 1;
    private int evals = 0;
    private int index = 0;
    private boolean multimodal = false;

    private Crossover crossover = new UniformCrossover();
    private ListCrossover listCrossover = new AllWithAllCrossover();
    private Selector selector = new TournamentSelection();
    private Mutator mutator = new NonUniformMutation(0.001);

    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new Individual[populationSize];

        for (int individuCounter = 0; individuCounter < populationSize; individuCounter++) {
            this.population[individuCounter] = new Individual(10);
        }
    }

     public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public int getEvals() {
        return evals;
    }

    public void setEvals(int evals) {
        this.evals = evals;
    }

    public boolean isMultimodal() {
        return multimodal;
    }

    public void setMultimodal(boolean multimodal) {
        this.multimodal = false;//multimodal;
    }

    @Override
    public boolean hasNext() {
        if (index < this.populationSize) {
            return true;
        }
        return false;
    }

    @Override
    public Individual next() {
        if (index < this.populationSize) {
            Individual result = population[index];
            index++;
            return result;
        } else {
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

    public double evaluate() {
        double maxFitness = Integer.MIN_VALUE;
        resetIndex();
        while (hasNext()) {
            Individual individual = next();
            Double fitness;
            fitness = evaluateIndividual(individual);
            if (fitness > maxFitness) {
                maxFitness = fitness;
            }
            individual.setFitness(fitness);
        }
        return maxFitness;
    }

    public double evaluate(Individual[] list) {
        double maxFitness = Integer.MIN_VALUE;
        Double fitness;
        for (Individual i : list) {
            fitness = evaluateIndividual(i);
            if (fitness > maxFitness) {
                maxFitness = fitness;
            }
        }
        return maxFitness;

    }

    public double evaluateIndividual(Individual individual) {
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
        for (int i = 0; i < populationSize; i++) {
            genomes[i] = population[i].getGenome();
        }

        resetIndex();
        Individual individual;
        while (hasNext()) {
            individual = next();

            double sumShared = 0;
            for (int i = 0; i < populationSize; i++) {
                double[] other = genomes[i];
                double distance = individual.distance(other);
                if (distance <= sigma) {
                    double shared = (1 - Math.pow(distance / sigma, alpha));
                    sumShared += shared;
                }
            }
            double sharedFitness = individual.getFitness() / sumShared;
            individual.setSharedFitness(sharedFitness);
        }
    }

    public void newGeneration() {
        //TODO Maybe more children from more couples
        final int num_children = 50;
        Individual[] parents = selector.select(num_children, population);
        Individual[] childs = listCrossover.combinelist(parents, crossover);
        Individual[] combined = Utils.mergeIndividualLists(parents, childs);
        // Evaluate population
        evaluate(combined);
        population = selector.select(populationSize, combined);
     }

}


