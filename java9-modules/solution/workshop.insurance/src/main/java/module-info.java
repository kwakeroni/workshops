module workshop.insurance {

    requires transitive workshop.family;

    exports workshop.insurance;

    uses workshop.insurance.Policy;
}