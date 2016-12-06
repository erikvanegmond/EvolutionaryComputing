import java.util.Random;

public class NoMutation implements Mutator {

    private double min = -50;
    private double max = 50;

    double sigma;

    public NoMutation(double sigma){
        this.sigma = sigma;
    }

    public Individual mutate(Individual child) {
        return child;
    }
}