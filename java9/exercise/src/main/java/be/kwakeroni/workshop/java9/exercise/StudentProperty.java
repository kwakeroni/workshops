package be.kwakeroni.workshop.java9.exercise;

enum StudentProperty {
    FIRST_NAME, LAST_NAME, AGE;

    static StudentProperty ofHeader(String header) {
        final StudentProperty property;
        switch (header.toLowerCase()) {
            case "firstname":
                property = FIRST_NAME;
                break;
            case "lastname":
                property = LAST_NAME;
                break;
            case "age":
                property = AGE;
                break;
            default:
                throw new IllegalArgumentException(header);
        }
        ;
        return property;
    }
}
