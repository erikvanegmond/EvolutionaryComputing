import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual implements Comparable<Individual>{
    private final Double minDouble = -Double.MAX_VALUE;
    private double[] genome;
    private Double fitness = minDouble;
    private double min = -50;
    private double max = 50;

    public Individual(int genomeSize){
        this.genome = new double[genomeSize];

        Random rand = new Random();
        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = min + (rand.nextDouble() * ((max - min) + 1));
        }
    }

    public Individual(double[] genome){
        this.genome = genome;
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

    public void mutate() {
        //TODO improve mutate function

        Random rand = new Random();
        for(int i=0; i < genome.length; i++){
            if (rand.nextDouble() > 0.8){
                genome[i] += rand.nextGaussian();
                //Stay within the search range.
                if(genome[i] < -50){
                    genome[i] = -50;
                }else if(genome[i] > 50){
                    genome[i] = 50;
                }

            }
        }

    }

}
