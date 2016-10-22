import java.util.Random;

public class UniformMutation implements Mutator {
    // can be used to get out of a local optimum

    private double min = -50;
    private double max = 50;

    @Override
    public Individual mutate(Individual child) {
        double mutationChance = 0.3;
        Random rand = new Random();
        double[] genome = child.getGenome();
        for (int i = 0; i < genome.length; i++) {
            if (rand.nextDouble() < mutationChance) {
                genome[i] += newAllele();
            }
        }
        return new Individual(genome);
    }

    public double newAllele(){
        Random rand = new Random();
        return min + (rand.nextDouble() * ((max - min) + 1));
    }

}