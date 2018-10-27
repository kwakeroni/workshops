package workshop.insurance;

import workshop.family.Family;

public interface Policy {

    public String generatePolicyData(Family family);

    public static Policy uninsured() {
        return new Policy() {
            @Override
            public String generatePolicyData(Family family) {
                return "{\"insured-family\": null}";
            }
        };
    }

}
