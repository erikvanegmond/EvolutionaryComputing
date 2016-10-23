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
            int parentIndex = selectIndividualForTournament(sample1, initialFitness, selectFrom);
            parents[i] = selectFrom[parentIndex];
        }
        return parents;
    }

//    //Old version
//    public Individual[] select(int n, Individual[] selectFrom) {
//    // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
//    // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
//    double initialFitness = -Double.MAX_VALUE;
//
//    // Find the index for the first parent
//    List<Integer> populationRange = range(0, selectFrom.length - 1);
//    List<Integer> sample1 = sample(populationRange);
//    int parentIndex1 = selectIndividualForTournament(sample1, initialFitness, selectFrom);
//
//    // Remove first parent from list of possibilities
//    Collections.sort(populationRange);
//            populationRange.remove(parentIndex1);
//
//            // Find the index for the second parent
//            List<Integer> sample2 = sample(populationRange);
//    int parentIndex2 = selectIndividualForTournament(sample2, initialFitness, selectFrom);
//
//    Individual[] parents = {selectFrom[parentIndex1], selectFrom[parentIndex2]};
//    return parents;
//    }

    private int selectIndividualForTournament(List<Integer> indexSample, double fitnessBestFit, Individual[] selectFrom) {
        int individualIndex = -1;
        for (int indexCounter = 0; indexCounter < tournamentSampleSize; indexCounter++) {
//            System.out.println(indexCounter);
//            System.out.println(fitnessBestFit);
            int indexFromSample = indexSample.get(indexCounter);
            double individualFitness;
            individualFitness = selectFrom[indexFromSample].getFitness();
//            System.out.println(individualFitness);
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

//    private Individual[] tournamentParents() {
//        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
//        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
//        double initialFitness = -Double.MAX_VALUE;
//
//        // Find the index for the first parent
//        List<Integer> populationRange = range(0, populationSize - 1);
//        List<Integer> sample1 = sample(populationRange);
//        int parentIndex1 = selectIndividualForTournament(sample1, "best", initialFitness);
//
//        // Remove first parent from list of possibilities
//        Collections.sort(populationRange);
//                populationRange.remove(parentIndex1);
//
//                // Find the index for the second parent
//                List<Integer> sample2 = sample(populationRange);
//        int parentIndex2 = selectIndividualForTournament(sample2, "best", initialFitness);
//
//        Individual[] parents = {population[parentIndex1], population[parentIndex2]};
//        return parents;
//        }
//
//    private int tournamentDying(){
//        // tournament selection: to select one individual, T (in this case tournamentSampleSize) individuals are uniformly
//        // chosen, and the best of these T is returned (from the paper Evolutionary Computing by mr Eiben)
//
//        double initialFitness = Double.MAX_VALUE;
//
//        // Find the index for the one who will be dying
//        List<Integer> populationRange = range(0, populationSize - 1);
//        List<Integer> sample1 = sample(populationRange);
//        int dyingIndex = selectIndividualForTournament(sample1, "worst", initialFitness);
//
//        return dyingIndex;
//    }
//
//    private List<Integer> sample(List<Integer> listForSample) {
//        Collections.shuffle(listForSample);
//        List<Integer> sample = listForSample.subList(0, tounamentSampleSize);
//        return sample;
//    }
//
//    private int selectIndividualForTournament(List<Integer> indexSample, String tournamentType, double fitnessBestFit) {
//        int individualIndex = -1;
//        for (int indexCounter = 0; indexCounter < tounamentSampleSize; indexCounter++) {
//            int indexFromSample = indexSample.get(indexCounter);
//            double individualFitness;
//            if(multimodal){
//                individualFitness = population[indexFromSample].getSharedFitness();
//            }else {
//                individualFitness = population[indexFromSample].getFitness();
//            }
//            if (tournamentType.equals("best")) {
//                if (individualFitness > fitnessBestFit) {
//                    fitnessBestFit = individualFitness;
//                    individualIndex = indexFromSample;
//                } else {
//                    continue;
//                }
//            } else if (tournamentType.equals("worst")) {
//                if (individualFitness < fitnessBestFit) {
//                    fitnessBestFit = individualFitness;
//                    individualIndex = indexFromSample;
//                } else {
//                    continue;
//                }
//            } else {
//                System.out.println("not a known tournamentType");
//            }
//        }
//        return individualIndex;
//    }