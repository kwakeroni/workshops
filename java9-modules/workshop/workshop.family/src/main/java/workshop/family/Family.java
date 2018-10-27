package workshop.family;

import workshop.animal.Animal;
import workshop.person.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static workshop.animal.Animal.SANTAS_LITTLE_HELPER;
import static workshop.person.Person.*;

public class Family {

    private final Set<Person> members;
    private final Animal pet;

    public Family(Set<Person> members, Animal pet) {
        this.members = Collections.unmodifiableSet(new HashSet<>(members));
        this.pet = pet;
    }

    public Set<Person> getMembers() {
        return members;
    }

    public Animal getPet() {
        return pet;
    }


    public static Family SOME_FAMILY = new Family(
            Set.of(BART, LISA, MAGGIE, MARGE, HOMER),
            SANTAS_LITTLE_HELPER
    );

    public static void main(String[] args) {
        System.out.println("Some Family: ");
        System.out.println("Members: " +
                SOME_FAMILY.getMembers().stream().map(Person::getFirstName).collect(Collectors.joining(", ")));
        System.out.println("Pet: " + SOME_FAMILY.getPet().getName() + " (" + SOME_FAMILY.getPet().getSpecies().getName() + ")");
    }

}
