import org.vu.contest.ContestEvaluation;

import java.util.*;

import org.vu.contest.ContestEvaluation;

import java.util.*;
import java.util.*;

class Population extends BasePopulation{

    private int tounamentSampleSize = 18;
    private String parentSelector = "best";
    private String typeCrossOver = "uniform";
    private double alphaBlend = 0.4;


    public Population(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        super(populationSize, evaluations_limit_, evaluation);
        mutationRate = 0.5;
    }

    public void newGeneration() {
        //TODO Maybe more children from more couples
//        final int numChildren = 50; //this affects the nochangecounter

        // Select 10 parents and let all of them mate with all of them
        // Now with tournament selection, can be changed to for example rank selection
        Individual[] parents = getParents(10);
        // Apply crossover / mutation operators -> Create offspring
        Individual[] children = generateOffspring(parents);
        int numChildren = children.length;

        for(int i=0; i<numChildren; i++) {
            evaluateIndividual(children[i]);
            //Replace the person who has lost in the tournament with the child
            int indexDying = tournamentDying();
            population[indexDying] = children[i];
        }
        // Evaluate population
        double best = evaluate();
        if(best > this.best){
            this.best = best;
            this.noChangeCounter = 0;
        }else{
            this.noChangeCounter++;
        }
//        System.out.println(best +" "+this.noChangeCounter);

        if (multimodal) {
            sharedFitness();
        }
    }

    private Individual[] getParents(int numParents) {
        Individual[] parents = tournamentParents(numParents);
        return parents;
    }

    private Individual[] tournamentParents(int numParents) {
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
        double initialFitness = -Double.MAX_VALUE;
        Individual[] parents = new Individual[numParents];

        // Find the index for the first parent
        List<Integer> populationRange = range(0, populationSize - 1);

        for(int i=0; i<numParents; i++) {
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

    private int tournamentDying(){
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
            if(multimodal){
                individualFitness = population[indexFromSample].getSharedFitness();
            }else {
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
        int genomeLenght = parents[0].getGenome().length;
        int nParents = parents.length;
        int nParentsPerRecombination = 2;
        int numberChildren = nCombinations(nParents, nParentsPerRecombination);
        Individual[] children = new Individual[numberChildren];
        int n = 0;
        for(int j=0; j<parents.length-1; j++) {
            for(int k=j+1; k<parents.length; k++) {
                n += 1;
                Individual[] currentParents = new Individual[nParentsPerRecombination];
                currentParents[0] = parents[j];
                currentParents[1] = parents[k];
                if (parents != null) {
                    double[] childGenome = new double[genomeLenght];
                    switch (typeCrossOver) {
                        case "uniform":
                            childGenome = uniformCrossOver(parents, childGenome, nParentsPerRecombination, genomeLenght);
                            break;
                        case "blend":
                            childGenome = blendCrossOver(parents, childGenome, nParentsPerRecombination, genomeLenght);
                            break;
                        default:
                            childGenome = blendCrossOver(parents, childGenome, nParentsPerRecombination, genomeLenght);
                            break;
                    }
                    Individual child = new Individual(childGenome);
                    child.mutate(mutationRate);
                    children[n-1] = child;
                } else {
                    continue;
                }
            }
        }
        System.out.println("n");
        System.out.println(n);
        return children;
    }


    public static int nCombinations(int setSize, int combinationSize){
        int fSet = factorial(setSize);
        int fCom = factorial(combinationSize);
        int fSetCom = factorial(setSize-combinationSize);
        int combinations = fSet/(fCom*(fSetCom));
        return combinations;
    }

    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
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

    private double[] randomBlendCrossOver(Individual[] parents, double[] childGenome, int nParents, int genomeLenght){
        Random rand = new Random();


        double[] weights = new double[nParents];
        double sum = 0;

        for(int i=0; i<nParents; i++) {
            weights[i] = rand.nextDouble();
            sum += weights[i];
        }
        for(int i=0; i<nParents; i++) {
            weights[i] /= sum;
        }

        for(int i=0; i<genomeLenght; i++){
            double newGene = 0;
            for(int j=0; j<nParents; j++){
                double gene = parents[j].getGenome()[i];
                newGene += gene * weights[j];
            }
            childGenome[i] = newGene;
        }
        return childGenome;
    }

    private double[] randomBlendCrossOver(Individual[] parents, double[] childGenome, int nParents, int genomeLenght){
        Random rand = new Random();


        double[] weights = new double[nParents];
        double sum = 0;

        for(int i=0; i<nParents; i++) {
            weights[i] = rand.nextDouble();
            sum += weights[i];
        }
        for(int i=0; i<nParents; i++) {
            weights[i] /= sum;
        }

        for(int i=0; i<genomeLenght; i++){
            double newGene = 0;
            for(int j=0; j<nParents; j++){
                double gene = parents[j].getGenome()[i];
                newGene += gene * weights[j];
            }
            childGenome[i] = newGene;
        }
        return childGenome;
    }

}

