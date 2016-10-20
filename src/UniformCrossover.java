import java.util.Random;

/**
 * Created by erikv on 20-10-2016.
 */
public class UniformCrossover implements Crossover {
    @Override
    public Individual combine(Individual[] parents) {
        int nParents = parents.length;
        int genomeLenght = parents[0].getGenome().length;
        double[] childGenome = new double[genomeLenght];

        Random rand = new Random();
        for (int i = 0; i < genomeLenght; i++) {
            int randomParent = rand.nextInt(nParents);
            childGenome[i] = parents[randomParent].getGenome()[i];
        }
        Individual child = new Individual(childGenome);
        return child;
    }
}
