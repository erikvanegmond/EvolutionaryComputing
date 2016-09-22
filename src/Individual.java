import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual {

    private double[] genome;

    public Individual(int genomeSize){
        this.genome = new double[genomeSize];

        Random rand = new Random();
        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = rand.nextInt(100)-50;
        }
        System.out.println(this.genome);
    }
}
