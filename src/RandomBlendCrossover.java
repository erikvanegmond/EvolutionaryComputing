import java.util.Random;

/**
 * Created by erikv on 20-10-2016.
 */
public class RandomBlendCrossover implements Crossover {
    @Override
    public Individual combine(Individual[] parents) {
        int nParents = parents.length;
        int genomeLenght = parents[0].getGenome().length;
        double[] childGenome = new double[genomeLenght];

        Random rand = new Random();


        double[] weights = new double[nParents];
        double sum = 0;

        for (int i = 0; i < nParents; i++) {
            weights[i] = rand.nextDouble();
            sum += weights[i];
        }
        for (int i = 0; i < nParents; i++) {
            weights[i] /= sum;
        }

        for (int i = 0; i < genomeLenght; i++) {
            double newGene = 0;
            for (int j = 0; j < nParents; j++) {
                double gene = parents[j].getGenome()[i];
                newGene += gene * weights[j];
            }
            childGenome[i] = newGene;
        }
        Individual child = new Individual(childGenome);
        return child;
    }

}
