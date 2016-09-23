import java.util.Iterator;
import java.util.NoSuchElementException;

class Population  implements Iterator<Individual> {

    Individual[] population;
    private int populationSize;
    private int index;


    public Population(int populationSize) {
        this.populationSize = populationSize;
        this.population = new Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new Individual(10);
            System.out.println(this.population[individuCounter]);
        }
    }

    @Override
    public boolean hasNext() {
        if(index < this.populationSize ){
            return true;
        }
        return false;
    }

    @Override
    public Individual next() {
        if(index < this.populationSize) {
            Individual result = this.population[index];
            index++;
            return result;
        }else{
            NoSuchElementException e = new NoSuchElementException("Element does not exist");
            throw e;
        }
    }

    @Override
    public void remove() {
        UnsupportedOperationException e = new UnsupportedOperationException();
        throw e;
    }

    public void reset() {
        this.index = 0;
    }
}
