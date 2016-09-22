class Population {

    Individual[] population;
    public Population(int populationSize) {
        this.population = new Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            this.population[individuCounter] = new Individual(10);
            System.out.println(individuCounter);
        }
    }
}
