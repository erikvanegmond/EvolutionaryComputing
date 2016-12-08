
import org.vu.contest.ContestEvaluation;

import java.util.Properties;

/**
 * Created by Frederik on 16.10.2016.
 * Found at https://github.com/alponomar/ECSuccess/blob/67ac1c5f5bf32b9b2837d993d670d0562760ecc3/src/functions/SchwefelEvaluation.java
 */
public class SchwefelEvaluation implements ContestEvaluation{
    // Evaluations budget
    private final static int EVALS_LIMIT_ = 100000;
    // The base performance. It is derived by doing  random search on the F&P function (see function method) with the same
    //  amount of evaluations
    private final static double BASE_ = 1.0;
    // The minimum of the sphere function
    private final static double ftarget_=0;

    // Best fitness so far
    private double best_;
    // Evaluations used so far
    private int evaluations_;

    // Properties of the evaluation
    private String multimodal_ = "true";
    private String regular_ = "true";
    private String separable_ = "false";
    private String evals_ = Integer.toString(EVALS_LIMIT_);

    private static double ALPHA_ = 10;

    public SchwefelEvaluation()
    {
        best_ = 0;
        evaluations_ = 0;
    }


    private double function(double[] x)
    {
        int dim = 10;

        double sum = 0;

        for(int i=0; i<dim; i++){
            double x_i = x[i];  // + 420.9687; // offset of minimum
            if (Math.abs(x_i) > 500) return 100000.0;
            sum += x_i * Math.sin(Math.sqrt(Math.abs(x_i)));
        }
        return 418.9829*dim - sum;
    }

    @Override
    public Object evaluate(Object result)
    {
        // Check argument
        if(!(result instanceof double[])) throw new IllegalArgumentException();
        double ind[] = (double[]) result;
        if(ind.length!=10) throw new IllegalArgumentException();

        if(evaluations_>EVALS_LIMIT_) return null;

        // Transform function value (F&P is minimization).
        // Normalize using the base performance
        double f = 10 - 10*( (function(ind)-ftarget_) / BASE_ ) ;
        if(f>best_) best_ = f;
        evaluations_++;

        return new Double(f);
    }

    @Override
    public Object getData(Object arg0)
    {
        return null;
    }

    @Override
    public double getFinalResult()
    {
        return best_;
    }

    @Override
    public Properties getProperties()
    {
        Properties props = new Properties();
        props.put("Multimodal", multimodal_);
        props.put("Regular", regular_);
        props.put("Separable", separable_);
        props.put("Evaluations", evals_);
        return props;
    }
}