import java.util.*;

public class TournamentSelection implements Selector{

    private int tournamentSampleSize = 18;

    @Override
    public Individual[] select(int n,Individual[] selectFrom){
        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)

        double initialFitness = -Double.MAX_VALUE;
        Individual[] parents = new Individual[n];
        int populationSize = selectFrom.length;

        // Find the index for the first parent
        List<Integer> populationRange = range(0, populationSize - 1);

        for (int i = 0; i < n; i++) {
            List<Integer> sample1 = sample(populationRange);
            int parentIndex = selectIndividualForTournament(sample1, "best", initialFitness, selectFrom);
            parents[i] = selectFrom[parentIndex];
        }
        return parents;
    }

    private int selectIndividualForTournament(List<Integer> indexSample, String tournamentType,
                                             double fitnessBestFit, Individual[] selectFrom) {
        int individualIndex = -1;
        for (int indexCounter = 0; indexCounter < tournamentSampleSize; indexCounter++) {
            int indexFromSample = indexSample.get(indexCounter);
            double individualFitness;
            individualFitness = selectFrom[indexFromSample].getFitness();
            if (individualFitness > fitnessBestFit) {
                fitnessBestFit = individualFitness;
                individualIndex = indexFromSample;
            } else {
                continue;
            }
        }
        return individualIndex;
    }

    private List<Integer> sample(List<Integer> listForSample) {
        Collections.shuffle(listForSample);
        List<Integer> sample = listForSample.subList(0, tournamentSampleSize);
        return sample;
    }

    private List<Integer> range(int min, int max) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        return list;
    }


}