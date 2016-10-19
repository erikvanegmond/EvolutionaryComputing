import org.vu.contest.ContestEvaluation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class DiffPopulation extends BasePopulation{

    //mutationRate [0,2]
    private double crossoverProbability = 0.5;

    public DiffPopulation(int populationSize, int evaluations_limit_, ContestEvaluation evaluation) {
        super(populationSize, evaluations_limit_, evaluation);
        mutationRate = 0.5;
    }

    public void newGeneration() {
        //create the new generation agent by agent
        //each agent is adapted using Differential Evolution and then tested
        //If it is fitter than the agent who was originally on that index, it will replace this agent
        for(int i=0; i< populationSize; i++) {
            // Select parents
            Individual[] parents = getParents(i,3);
            // Apply crossover / mutation operators -> Create offspring
            Individual currentAgent = population[i];
            Individual child = generateOffspring(currentAgent, parents);

            // if fitness child higher than former current agent,
            // replace the former current agent
            double fitnessCurrentAgent = evaluateIndividual(currentAgent);
            double fitnessChild = evaluateIndividual(child);
            if(fitnessCurrentAgent <= fitnessChild) {
                population[i] = child;
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
    }

    private Individual[] getParents(int currentIndividualIndex, int numParents) {
        // randomly select 3 agents who are not eachother and not the current individual
        // first generate list of all indexes in the population without current agent
        List<Integer> populationRange = range(0, populationSize - 1);
        populationRange.remove(currentIndividualIndex);

        // Get a sample of the indexes of the 3 parents by using either random selection or tournament selection
        int nParents = 3;
        Individual[] parents = null;
        parents = randomParentSelection(populationRange, nParents);
        return parents;
    }

    private Individual[] randomParentSelection(List<Integer> populationRange, int sampleSize){
        List<Integer> sample = sample(populationRange, sampleSize);
        Individual[] parents = {population[sample.get(0)], population[sample.get(1)], population[sample.get(2)]};
        return parents;
    }

    private List<Integer> sample(List<Integer> listForSample, int sampleSize) {
        Collections.shuffle(listForSample);
        List<Integer> sample = listForSample.subList(0, sampleSize);
        return sample;
    }

    private Individual generateOffspring(Individual currentAgent, Individual[] parents) {
        //Generate offspring
        if(parents != null) {
            int nParents = parents.length;
            int genomeLenght = parents[0].getGenome().length;
            double[] childGenome = new double[genomeLenght];

            childGenome = differentialCrossOver(currentAgent, parents, childGenome, genomeLenght);

            Individual child = new Individual(childGenome);
            return child;

        }else{
            return null;
        }
    }

    private double[] differentialCrossOver(Individual currentAgent, Individual[] parents, double[] childGenome, int genomeLenght){
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
                childGenome[j] = genP1 + mutationRate * (genP2 - genP3);
            }
            else{
                childGenome[j] = currentAgent.getGenome()[j];
            }
        }
        return childGenome;
    }

}


