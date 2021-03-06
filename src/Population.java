import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population implements Iterator<Individual>{

    Individual[] population;
    ContestEvaluation evaluation_;
    private int populationSize;
    private int genomeSize = 10;
    private int evaluations_limit_;

    private Crossover crossover = new UniformCrossover();
    private ListCrossover listCrossover = new AllWithAllCrossover();
    private Selector selector = new TournamentSelection();
    private Mutator mutator = new NonUniformMutation(0.001);

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    private double mutationRate=1;
    private int evals = 0;
    private int index = 0;
    private int tounamentSampleSize = 18;
    private boolean multimodal = false;
    private String typeCrossOver = "blend";
    private double alphaBlend = 0.4;
    private int noChangeCounter = 0;

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


    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        this.populationSize = populationSize;
        this.evaluations_limit_ = evaluations_limit_;
        this.evaluation_ = evaluation;
        this.population = new Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new Individual(10);
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

    private int getIndex() {
        return this.index;
    }

    private void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public double evaluate() {
        double maxFitness = Integer.MIN_VALUE;
//        Individual bestIndividual = null;
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
        for(Individual i: list){
            fitness = evaluateIndividual(i);
            if (fitness > maxFitness) {
                maxFitness = fitness;
            }
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

    public void newGeneration() {
        //TODO Maybe more children from more couples
        final int num_children = 50; //this affects the nochangecounter
        Individual[] children = new Individual[num_children];



//        for(int i=0; i< num_children; i++) {
//            // Select parents
//            Individual[] parents = getParents(2);
//            // Apply crossover / mutation operators -> Create offspring
//            children[i] = generateOffspring(parents);
//        }

        Individual[] parents = selector.select(50, population);
        Individual[] childs = listCrossover.combinelist(parents,crossover);
//        for(int i=0; i< num_children; i++) {
//            evaluateIndividual(children[i]);
//            //Replace the person who has lost in the tournament with the child
//            int indexDying = tournamentDying();
//            population[indexDying] = children[i];
//        }

        Individual[] combined = Utils.mergeIndividualLists(parents, childs);
        // Evaluate population
        double best = evaluate(combined);
        population = selector.select(populationSize, combined);
        if(best > this.best){
            this.best = best;
            this.noChangeCounter = 0;
        }else{
            this.noChangeCounter++;
        }
        System.out.println(best +" "+this.noChangeCounter);

        if (multimodal) {
            sharedFitness();
        }
    }

    private Individual[] getParents(int numParents) {
        Individual[] parents = selector.select(numParents, population);
        return parents;
    }

    private Individual generateOffspring(Individual[] parents) {
        //Generate offspring based on uniform crossover
        if(parents != null) {
            int nParents = parents.length;
            int genomeLenght = parents[0].getGenome().length;
            double[] childGenome = new double[genomeLenght];
            switch (typeCrossOver) {
                case "uniform":
                    childGenome = uniformCrossOver(parents, childGenome, nParents, genomeLenght);
                    break;
                case "blend":
                    childGenome = blendCrossOver(parents, childGenome, nParents, genomeLenght);
                    break;
                default:
                    childGenome = blendCrossOver(parents, childGenome, nParents, genomeLenght);
                    break;
            }
            Individual child = new Individual(childGenome);
            child.mutate(mutationRate);
            return child;
        }else{
            return null;
        }
    }

    private double[] uniformCrossOver(Individual[] parents, double[] childGenome, int nParents, int genomeLenght){
        Random rand = new Random();
        for(int i=0; i<genomeLenght; i++){
            int randomParent = rand.nextInt(nParents);
            childGenome[i] = parents[randomParent].getGenome()[i];
        }
        return childGenome;
    }

    private double[] blendCrossOver(Individual[] parents, double[] childGenome, int nParents, int genomeLenght){
        // create new gene out of random sample in the range between genes parents
        double biggestGene = -Double.MAX_VALUE;
        double smallestGene = Double.MAX_VALUE;
        Random rand = new Random();
        // loop over the genomes of the parents and determine per gene which one is the lowest
        // and which one is the highest gene value, so they can be used in the blending for the
        // gene of the child
        for(int i=0; i<genomeLenght; i++){
            for(int j=0; j<nParents; j++){
                double gene = parents[j].getGenome()[i];
                if (gene>biggestGene) {
                    biggestGene = gene;
                }
                if (gene<smallestGene) {
                    smallestGene = gene;
                }
                else {
                    continue;
                }
            }
            double d = biggestGene-smallestGene;
            double lowerBound = smallestGene - (alphaBlend * d) ;
            double upperBound = biggestGene + (alphaBlend * d) ;
            double randomDouble = rand.nextDouble();
            // generating a random double between lowerBound and the upperBound
            childGenome[i] = lowerBound + ((upperBound - lowerBound) * randomDouble);
        }
        return childGenome;
    }

}


