import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual {

    private double[] genome;
    private double fitness = 0;

    public Individual(int genomeSize){
        this.genome = new double[genomeSize];

        Random rand = new Random();
        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = rand.nextInt(100)-50;
        }
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
}
