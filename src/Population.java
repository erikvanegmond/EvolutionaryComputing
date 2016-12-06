import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population implements Iterator<Individual> {

    private Individual[] population;
    private ContestEvaluation evaluation_;
    private int populationSize;
    private int genomeSize = 10;
    private int evaluations_limit_;
    private int evals = 0;
    private int index = 0;
    private boolean multimodal = false;
    private Crossover crossover = new UniformCrossover();
    private CrossoverList crossoverList = new CrossoverListAllWithAll();
    private Selector selector = new SelectTournament();
    private Mutator mutator = new NonUniformMutation(0.005);
    private ListMutation listMutation = new ListMutation();

    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new Individual[populationSize];

        for (int individuCounter = 0; individuCounter < populationSize; individuCounter++) {
            this.population[individuCounter] = new Individual(genomeSize);
        }
    }

    public void newGeneration() {
        final int num_children = 50;
        Individual[] parents = selector.select(num_children, population);
        Individual[] childs = crossoverList.combinelist(parents, crossover);
        Individual[] mutatedChilds = listMutation.mutatelist(childs, mutator);
        Individual[] combined = Utils.mergeIndividualLists(parents, mutatedChilds);
        // Evaluate population
        evaluate(combined);
        population = selector.select(populationSize, combined);
    }

    public boolean isMultimodal() {
        return multimodal;
    }

    public void setMultimodal(boolean multimodal) {
        this.multimodal = false;//multimodal;
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

}


