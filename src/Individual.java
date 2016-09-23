import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual implements Comparable<Individual>{

    private double[] genome;
    private Double fitness = Double.MIN_VALUE;
    private double min = -50;
    private double max = 50;

    public Individual(int genomeSize){
        this.genome = new double[genomeSize];

        Random rand = new Random();
        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = rand.nextInt(100)-50;
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
        if(fitness != Double.MIN_VALUE){
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
        Random rand = new Random();
        int randomIndex = rand.nextInt(genome.length);
        genome[randomIndex] += (rand.nextInt(10)-20);
        if(genome[randomIndex] < -50){
            genome[randomIndex] = -50;
        }else if(genome[randomIndex] > 50){
            genome[randomIndex] = 50;
        }

    }
}
