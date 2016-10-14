import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class DiffIndividual implements Comparable<DiffIndividual>{
    private final Double minDouble = -Double.MAX_VALUE;
    private double[] genome;
    private Double fitness = minDouble;
    private Double sharedFitness = minDouble;
    private double min = -50;
    private double max = 50;

    public DiffIndividual(int genomeSize){
        this.genome = new double[genomeSize];

        for(int i = 0; i<genomeSize; i++) {
            this.genome[i] = newAllele();
        }
    }

    public DiffIndividual(double[] genome){
        this.genome = genome;
    }

    public double newAllele(){
        Random rand = new Random();
        return min + (rand.nextDouble() * ((max - min) + 1));
    }

    public double getSharedFitness() {
        return sharedFitness;
    }

    public void setSharedFitness(double sharedFitness) {
        this.sharedFitness = sharedFitness;
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
    public int compareTo(DiffIndividual other) {
        double otherFitness = other.getFitness();
        return (int) (otherFitness - getFitness());
    }

    public void mutate(double mutationRate){
        String mutation = "nonuniformMutation";
        switch (mutation){
            case "nonuniformMutation":
                nonuniformMutation(mutationRate);
            case "uniformMutation":
                uniformMutation();
                break;
        }

    }

    public void nonuniformMutation(double sigma) {
        final double mutationChance = 0.3;
        Random rand = new Random();
        for(int i=0; i < genome.length; i++){
            if (rand.nextDouble() < mutationChance){
                genome[i] += rand.nextGaussian()*sigma;
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

    public double distance(DiffIndividual other){
        int genome_length = this.genome.length;
        int sum = 0;
        for(int i=0; i<genome_length; i++){
            double diff = Math.abs(this.genome[i] - other.getGenome()[i]);
            sum += Math.pow(diff, 2);
        }
        return Math.sqrt(sum);
    }

    public double distance(double[] otherGenome){
        int genome_length = this.genome.length;
        int sum = 0;
        for(int i=0; i<genome_length; i++){
            double diff = Math.abs(this.genome[i] - otherGenome[i]);
            sum += diff;
        }
        return sum;
    }

}
