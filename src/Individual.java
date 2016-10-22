//import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
//import org.apache.commons.math3.linear.ArrayRealVector;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by erikv on 14-9-2016.
 */
public class Individual implements Comparable<Individual>{
    private final double minDouble = -Double.MAX_VALUE;
    private double[] genome;
    private double[] sigmas;
    private double[] alphas;
    private double fitness = minDouble;
    private double sharedFitness = minDouble;
    private double min = -5;
    private double max = 5;
    private int genomeSize;
    private double tau;
    private double tau_prime;

    public Individual(int genomeSize){
        this.genomeSize = genomeSize;
        this.genome = new double[genomeSize];
        this.sigmas = new double[genomeSize];
        this.alphas = new double[genomeSize*(genomeSize-1)/2];
        this.tau = 1/(Math.sqrt(2*Math.sqrt(genomeSize)));
        this.tau_prime = 1/(Math.sqrt(2 * genomeSize));

        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = newAllele();
            this.sigmas[i] = 1;
        }
    }

    public Individual(double[] genome){
        this.genome = genome;
        genomeSize = genome.length;
        this.sigmas = new double[genomeSize];
        this.alphas = new double[genomeSize*(genomeSize-1)/2];
        this.tau = 1/(Math.sqrt(2*Math.sqrt(genomeSize)));
        this.tau_prime = 1/(Math.sqrt(2 * genomeSize));

        for(int i = 0; i<genomeSize; i++){
            this.genome[i] = newAllele();
            this.sigmas[i] = 1;
        }
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
    public int compareTo(Individual other) {
        double otherFitness = other.getFitness();
        return (int) (otherFitness - getFitness());
    }

//    public void mutate(double mutationRate){
//        String mutation = "nonuniformMutation";
//        switch (mutation){
//            case "nonuniformMutation":
//                nonuniformMutation(mutationRate);
//            break;
//        }
//
//    }

    private double[][] covarianceMatrix(){
        double[][] cArray = new double[this.sigmas.length][this.sigmas.length];
        for(int i=0; i<this.sigmas.length; i++){
            double i2 =  this.sigmas[i]*this.sigmas[i];
            for(int j=0; i<this.sigmas.length; i++){
                double j2 =  this.sigmas[j]*this.sigmas[j];
                if(i==j){
                    cArray[i][j] = i2;
                }else{
                    double tan = 2 * cArray[i][j]/(i2 - j2);
                    cArray[i][j] = 0.5 * (i2 - j2) * tan;
                }
            }
        }
        return cArray;
    }
    private void correlatedMutation() {
        Random rand = new Random();

        //calculate the c matrix;



        //update sigma
        for(int i=0; i<this.sigmas.length; i++){
            this.sigmas[i] = this.sigmas[i] * Math.exp(this.tau_prime * rand.nextDouble() + this.tau * rand.nextDouble());
        }

        //update alphas
        double beta = Math.toRadians(5);
        for(int j=0; j<this.alphas.length; j++){
            this.alphas[j] = this.alphas[j] + beta * rand.nextDouble();
        }

        double[][] cArray = covarianceMatrix();
//        MultivariateNormalDistribution multivariateDistribution = new MultivariateNormalDistribution(new double[this.genomeSize], cArray);


//        ArrayRealVector genomeVector = new ArrayRealVector(genome);
//        genomeVector.add(new ArrayRealVector(multivariateDistribution.sample()));
//        genome = genomeVector.toArray();
    }

    public double distance(Individual other){
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
