import java.util.Random;

public class NonUniformMutation implements Mutator {

    private double min = -50;
    private double max = 50;

    public Individual mutate(Individual child) {
        double mutationChance = 1;
        double sigma = 0.05;
        double[] genome = child.getGenome();
        Random rand = new Random();
        for (int i = 0; i < genome.length; i++) {
            if (rand.nextDouble() < mutationChance) {
                genome[i] += rand.nextGaussian() * sigma;
                //Stay within the search range.
                if (genome[i] < min) {
                    genome[i] = min;
                } else if (genome[i] > max) {
                    genome[i] = max;
                }
            }
        }
        return new Individual(genome);
    }
}