import org.vu.contest.ContestEvaluation;

import java.util.*;

class Population extends BasePopulation {

    private int tounamentSampleSize = 18;
    private Crossover crossover = new RandomBlendCrossover();
    private double alphaBlend = 0.4;


    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        super(populationSize, evaluations_limit_, evaluation);
        mutationRate = 0.01;
    }

    public double averageFitness(){
        double sum = 0;
        for(Individual individual : population){
            sum += individual.getFitness();
        }
        return sum/populationSize;
    }

    public void newGeneration() {
        //TODO Maybe more children from more couples
//        final int numChildren = 50; //this affects the nochangecounter

        // Select 10 parents and let all of them mate with all of them
        // Now with tournament selection, can be changed to for example rank selection
        Individual[] parents = getParents(50);
        // Apply crossover / mutation operators -> Create offspring
        Individual[] children = generateOffspring(parents);
        int numChildren = children.length;

        Individual[] combined = new Individual[population.length + children.length];
        System.arraycopy(population, 0, combined, 0, population.length);
        System.arraycopy(children, 0, combined, population.length, children.length);
        evaluate(combined);
        population = selectTopN(populationSize, combined);


//        for (int i = 0; i < numChildren; i++) {
//            evaluateIndividual(children[i]);
//            //Replace the person who has lost in the tournament with the child
//            int indexDying = tournamentDying();
//            if(population[indexDying].getFitness() < children[i].getFitness()) {
//                population[indexDying] = children[i];
//            }
//        }

        // Evaluate population
        double best = evaluate();
        if (best > this.best) {
            this.best = best;
            this.noChangeCounter = 0;
        } else {
            this.noChangeCounter++;
        }
        System.out.println(best +" "+ averageFitness());

        if (multimodal) {
            sharedFitness();
        }
    }

    private Individual[] getParents(int numParents) {
//        Individual[] parents = tournamentParents(numParents);
        Individual[] parents = selectTopN(numParents, population);
        return parents;
    }

    private Individual[] selectTopN(int n, Individual[] selectFrom){
        if(n < selectFrom.length) {
            Arrays.sort(selectFrom);
            Individual[] selected = Arrays.copyOfRange(selectFrom, 0, n);
            return selected;
        }else{
            return null;
        }
    }

    private Individual[] tournamentParents(int numParents) {
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
        double initialFitness = -Double.MAX_VALUE;
        Individual[] parents = new Individual[numParents];

        // Find the index for the first parent
        List<Integer> populationRange = range(0, populationSize - 1);

        for (int i = 0; i < numParents; i++) {
            List<Integer> sample1 = sample(populationRange);
            int parentIndex1 = selectIndividualForTournament(sample1, "best", initialFitness);
            parents[i] = population[parentIndex1];

            // Remove first parent from list of possibilities
//            Collections.sort(populationRange);
//            populationRange.remove(parentIndex1);
        }
//        // Find the index for the second parent
//        List<Integer> sample2 = sample(populationRange);
//        int parentIndex2 = selectIndividualForTournament(sample2, "best", initialFitness);

//                {population[parentIndex1], population[parentIndex2]};
        return parents;
    }

    private int tournamentDying() {
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)

        double initialFitness = Double.MAX_VALUE;

        // Find the index for the one who will be dying
        List<Integer> populationRange = range(0, populationSize - 1);
        List<Integer> sample1 = sample(populationRange);
        int dyingIndex = selectIndividualForTournament(sample1, "worst", initialFitness);

        return dyingIndex;
    }

    private List<Integer> sample(List<Integer> listForSample) {
        Collections.shuffle(listForSample);
        List<Integer> sample = listForSample.subList(0, tounamentSampleSize);
        return sample;
    }

    private int selectIndividualForTournament(List<Integer> indexSample, String tournamentType, double fitnessBestFit) {
        int individualIndex = -1;
        for (int indexCounter = 0; indexCounter < tounamentSampleSize; indexCounter++) {
            int indexFromSample = indexSample.get(indexCounter);
            double individualFitness;
            if (multimodal) {
                individualFitness = population[indexFromSample].getSharedFitness();
            } else {
                individualFitness = population[indexFromSample].getFitness();
            }
            if (tournamentType.equals("best")) {
                if (individualFitness > fitnessBestFit) {
                    fitnessBestFit = individualFitness;
                    individualIndex = indexFromSample;
                } else {
                    continue;
                }
            } else if (tournamentType.equals("worst")) {
                if (individualFitness < fitnessBestFit) {
                    fitnessBestFit = individualFitness;
                    individualIndex = indexFromSample;
                } else {
                    continue;
                }
            } else {
                System.out.println("not a known tournamentType");
            }
        }
        return individualIndex;
    }

    private Individual[] generateOffspring(Individual[] parents) {
        //Generate offspring based on uniform crossover or blend crossover
        // combines all parents with each other
        int nParents = parents.length;
        int n = 0;

        ListCrossover listCrossover = new AllWithAllCrossover();

        Individual[] children = listCrossover.combinelist(parents, crossover);

        for(int i=0; i<children.length; i++){
            children[i].mutate(mutationRate);
        }

        return children;
    }

}