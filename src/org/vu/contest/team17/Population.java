package org.vu.contest.team17;

class Population {
    player17.Individual[] population;

    public Population(int populationSize) {
        this.population = new player17.Individual[populationSize];

        for(int individuCounter = 0; individuCounter < populationSize; individuCounter++){
            System.out.println(this.population[individuCounter]);
        }
    }
}