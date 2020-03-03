package be.kwakeroni.workshop.java9.solution;

enum StudentProperty {
    FIRST_NAME, LAST_NAME, AGE;

    static StudentProperty ofHeader(String header) {
        return switch (header.toLowerCase()) {
            case "firstname" -> FIRST_NAME;
            case "lastname" -> LAST_NAME;
            case "age" -> AGE;
            default -> throw new IllegalArgumentException(header);
        };
    }
}
