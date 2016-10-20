import java.util.Random;

/**
 * Created by erikv on 20-10-2016.
 */
public class BlendCrossOver implements Crossover {
    double alphaBlend = 0.4;

    @Override
    public Individual combine(Individual[] parents) {
        int nParents = parents.length;
        int genomeLenght = parents[0].getGenome().length;
        double[] childGenome = new double[genomeLenght];

        // create new gene out of random sample in the range between genes parents
        double biggestGene = -Double.MAX_VALUE;
        double smallestGene = Double.MAX_VALUE;
        Random rand = new Random();
        // loop over the genomes of the parents and determine per gene which one is the lowest
        // and which one is the highest gene value, so they can be used in the blending for the
        // gene of the child
        for (int i = 0; i < genomeLenght; i++) {
            for (int j = 0; j < nParents; j++) {
                double gene = parents[j].getGenome()[i];
                if (gene > biggestGene) {
                    biggestGene = gene;
                }
                if (gene < smallestGene) {
                    smallestGene = gene;
                } else {
                    continue;
                }
            }
            double d = biggestGene - smallestGene;
            double lowerBound = smallestGene - (alphaBlend * d);
            double upperBound = biggestGene + (alphaBlend * d);
            double randomDouble = rand.nextDouble();
            // generating a random double between lowerBound and the upperBound
            childGenome[i] = lowerBound + ((upperBound - lowerBound) * randomDouble);
        }
        Individual child = new Individual(childGenome);
        return child;
    }
}
