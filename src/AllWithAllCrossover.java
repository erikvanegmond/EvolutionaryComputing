import java.util.ArrayList;

/**
 * Created by erikv on 20-10-2016.
 */
public class AllWithAllCrossover implements ListCrossover {

    @Override
    public Individual[] combinelist(Individual[] list, Crossover crossover) {
        //for now we only support combinations of 2
        ArrayList<Individual> returnList = new ArrayList<Individual>();

        int len = list.length;

        for(int i = 0; i < len; i++){
            for(int j = 0; j < i; j++){
                if(i != j){
                    Individual[] toCombine = {list[i], list[j]};
                    returnList.add(crossover.combine(toCombine));
                }
            }
        }
        Individual[] combinations = new Individual[returnList.size()];

        for(int i=0; i<returnList.size(); i++){
            combinations[i] = returnList.get(i);
        }

        return combinations;
    }
}
