import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual implements Comparable<Individual>{
    private final Double minDouble = -Double.MAX_VALUE;
    private double[] genome;
    private Double fitness = minDouble;
    private double min = 0;
    private double max = 10;

    public Individual(int genomeSize){
        this.genome = new double[genomeSize];

        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = newAllele();
        }
    }

    public Individual(double[] genome){
        this.genome = genome;
    }

    public double newAllele(){
        Random rand = new Random();
        return min + (rand.nextDouble() * ((max - min) + 1));
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public String toString(){
        return fitness + " " + Arrays.toString(this.genome);
    }

    public double[] getGenome() {
        return genome;
    }

    public boolean hasScore(){
        if(fitness != minDouble){
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Individual other) {
        double otherFitness = other.getFitness();
        return (int) (otherFitness - getFitness());
    }

    public void mutate(){
        String mutation = "nonuniformMutation";
        switch (mutation){
            case "nonuniformMutation":
                nonuniformMutation();
            case "uniformMutation":
                uniformMutation();
            break;
        }

    }

    public void nonuniformMutation() {
        final double mutationChance = 0.3;
        Random rand = new Random();
        for(int i=0; i < genome.length; i++){
            if (rand.nextDouble() < mutationChance){
                genome[i] += rand.nextGaussian();
                //Stay within the search range.
                if(genome[i] < min){
                    genome[i] = min;
                }else if(genome[i] > max){
                    genome[i] = max;
                }

            }
        }
    }

    public void uniformMutation(){
    // can be used to get out of a local optimum
        final double mutationChance = 0.03;
        Random rand = new Random();
        for(int i=0; i < genome.length; i++){
            if (rand.nextDouble() < mutationChance){
                genome[i] += newAllele();
            }
        }
    }

}
