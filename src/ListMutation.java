/**
 * Created by erikv on 20-10-2016.
 */
public class ListMutation {
    public Individual[] mutatelist(Individual[] lists, Mutator mutator) {
        Individual[] mutatedIndividuals = new Individual[lists.length];
        for (int i = 0; i < lists.length; i++) {
            mutatedIndividuals[i] = mutator.mutate(lists[i]);
        }
        return mutatedIndividuals;
    }
}
