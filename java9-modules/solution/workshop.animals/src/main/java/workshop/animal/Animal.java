package workshop.animal;

import workshop.animal.species.Species;

public class Animal {

    private final String name;
    private final Species species;

    public Animal(String name, Species species) {
        this.name = name;
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public static Animal of(String name, String species) {
        return new Animal(name, new Species(species));
    }

    public static final Animal SANTAS_LITTLE_HELPER = Animal.of("Santa's Little Helper", "dog");
}
