module workshop.family {
    requires transitive workshop.animals;
    requires transitive workshop.persons;

    exports workshop.family;
    opens workshop.family;
}