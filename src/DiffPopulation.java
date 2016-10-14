import org.vu.contest.ContestEvaluation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class DiffPopulation implements Iterator<DiffIndividual>{

    DiffIndividual[] population;
    ContestEvaluation evaluation_;
    private int populationSize;
    private int genomeSize = 10;
    private int evaluations_limit_;

    private int evals = 0;
    private int index = 0;
    private boolean multimodal = false;
    private int noChangeCounter = 0;

    //mutationConstant [0,2]
    private double mutationConstant = 1.5;
    private double crossoverProbability = 0.9;
    private String typeParentSelection = "tournament";
    private int tournamentSampleSize = 18;

    public int getEvals() {
        return evals;
    }

    public void setEvals(int evals) {
        this.evals = evals;
    }



    public int getNoChangeCounter() {
        return noChangeCounter;
    }

    public void setNoChangeCounter(int noChangeCounter) {
        this.noChangeCounter = noChangeCounter;
    }

    private double best = -Double.MAX_VALUE;


    public DiffPopulation(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new DiffIndividual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new DiffIndividual(10);
        }
    }

    public static List<Integer> range(int min, int max) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        return list;
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
    public DiffIndividual next() {
        if(index < this.populationSize) {
            DiffIndividual result = population[index];
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

    private int getIndex() {
        return this.index;
    }

    private void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public double evaluate() {
        double maxFitness = Integer.MIN_VALUE;
//        DiffIndividual bestIndividual = null;
        resetIndex();
        while (hasNext()) {
            DiffIndividual individual = next();
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

    public double evaluateIndividual(DiffIndividual individual){
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

//    public void sharedFitness() {
//        final int sigma = 7; // sugested to be between 5 and 10. p93 Introduction to Evolutionary Computing
//        final double alpha = 1;// linear shape for the sharing function.
//
//
//        double[][] genomes = new double[populationSize][genomeSize];
//        for(int i=0; i<populationSize; i++){
//            genomes[i] = population[i].getGenome();
//        }
//
//        resetIndex();
//        DiffIndividual individual;
//        while (hasNext()) {
//            individual = next();
//
//            double sumShared = 0;
//            for(int i=0; i<populationSize; i++){
//                double[] other = genomes[i];
//                double distance = individual.distance(other);
//                if(distance <= sigma){
//                    double shared = (1 - Math.pow(distance / sigma, alpha));
//                    sumShared += shared;
//                }
//            }
//            double sharedFitness = individual.getFitness()/sumShared;
//            individual.setSharedFitness(sharedFitness);
//        }
//    }

    public void newGeneration() {
        //create the new generation agent by agent
        //each agent is adapted using Differential Evolution and then tested
        //If it is fitter than the agent who was originally on that index, it will replace this agent
        for(int i=0; i< populationSize; i++) {
            // Select parents
            DiffIndividual[] parents = getParents(i,3);
            // Apply crossover / mutation operators -> Create offspring
            DiffIndividual currentAgent = population[i];
            DiffIndividual child = generateOffspring(currentAgent, parents);

            // if fitness child higher than former current agent,
            // replace the former current agent
            double fitnessCurrentAgent = evaluateIndividual(currentAgent);
            double fitnessChild = evaluateIndividual(child);
            if(fitnessCurrentAgent <= fitnessChild){
                population[i] = child;
                System.out.println("better!");
            }

        }
        // Evaluate population
        double best = evaluate();
        if(best > this.best){
            this.best = best;
            this.noChangeCounter = 0;
        }else{
            this.noChangeCounter++;
        }
        System.out.println(best +" "+this.noChangeCounter);

//        if (multimodal) {
//            sharedFitness();
//        }
    }

    private DiffIndividual[] getParents(int currentIndividualIndex, int numParents) {
        // randomly select 3 agents who are not eachother and not the current individual
        // first generate list of all indexes in the population without current agent
        List<Integer> populationRange = range(0, populationSize - 1);
        populationRange.remove(currentIndividualIndex);

        // Get a sample of the indexes of the 3 parents by using either random selection or tournament selection
        int sampleSize = 3;
        DiffIndividual[] parents = null;
        switch (typeParentSelection) {
            case "random":
                parents = randomParentSelection(populationRange, sampleSize);
                break;
            case "tournament":
                parents = tournamentParentSelection(populationRange, sampleSize);
                System.out.println("in tournament");
                break;
            default:
                parents = tournamentParentSelection(populationRange, sampleSize);
                break;
        }
        return parents;
    }

    private DiffIndividual[] randomParentSelection(List<Integer> populationRange, int sampleSize){
        List<Integer> sample = sample(populationRange, sampleSize);
        DiffIndividual[] parents = {population[sample.get(0)], population[sample.get(1)], population[sample.get(2)]};
        return parents;
    }

    private DiffIndividual[] tournamentParentSelection(List<Integer> populationRange, int numParents){
        int individualIndex = -1;
        double fitnessBestFit = -Double.MAX_VALUE;
        DiffIndividual[] parents = new DiffIndividual[numParents];
        for (int parentCounter = 0; parentCounter < numParents; parentCounter++){
            if (parentCounter > 0){
                populationRange.remove(parents[parentCounter-1]);
            }
            List<Integer> indexSample = sample(populationRange, tournamentSampleSize);
            for (int indexCounter = 0; indexCounter < tournamentSampleSize; indexCounter++) {
                int indexFromSample = indexSample.get(indexCounter);
                double individualFitness;
//            if(multimodal){
//                individualFitness = population[indexFromSample].getSharedFitness();
//            }else {
                individualFitness = population[indexFromSample].getFitness();
                if (individualFitness > fitnessBestFit) {
                    fitnessBestFit = individualFitness;
                    individualIndex = indexFromSample;
                } else {
                    continue;
                }
            parents[parentCounter] = population[individualIndex];
            }
        }

        return parents;
    }

    private List<Integer> sample(List<Integer> listForSample, int sampleSize) {
        Collections.shuffle(listForSample);
        List<Integer> sample = listForSample.subList(0, sampleSize);
        return sample;
    }

    private DiffIndividual generateOffspring(DiffIndividual currentAgent, DiffIndividual[] parents) {
        //Generate offspring
        if(parents != null) {
            int nParents = parents.length;
            int genomeLenght = parents[0].getGenome().length;
            double[] childGenome = new double[genomeLenght];

            childGenome = differentialCrossOver(currentAgent, parents, childGenome, genomeLenght);

            DiffIndividual child = new DiffIndividual(childGenome);
            return child;

        }else{
            return null;
        }
    }

    private double[] differentialCrossOver(DiffIndividual currentAgent, DiffIndividual[] parents, double[] childGenome, int genomeLenght){
        //Use all three parents and the current Agent to create the child of the currentAgent

        //First generate a random cut index
        int cutIndexR = ThreadLocalRandom.current().nextInt(0, 10 + 1);

        for(int j=0; j<genomeLenght; j++) {
            Random r = new Random();
            double randDouble = r.nextDouble();
            if (j == cutIndexR || randDouble < crossoverProbability) {
                double genP1 = parents[0].getGenome()[j];
                double genP2 = parents[1].getGenome()[j];
                double genP3 = parents[2].getGenome()[j];
                childGenome[j] = genP1 + mutationConstant * (genP2 - genP3);
            }
            else{
                childGenome[j] = currentAgent.getGenome()[j];
            }
        }
        return childGenome;
    }

}

