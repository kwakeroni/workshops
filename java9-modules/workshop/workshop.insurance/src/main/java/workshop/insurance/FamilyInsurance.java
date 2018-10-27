package workshop.insurance;


import workshop.family.Family;
import workshop.person.Person;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class FamilyInsurance {

    private final Policy policy;
    private final Family family;

    public FamilyInsurance(Policy policy, Family family) {
        this.policy = policy;
        this.family = family;
    }

    public int getPoolSize() {
        return family.getMembers().size();
    }

    public long getHighestAge() {
        LocalDate now = LocalDate.now();
        return family.getMembers()
                .stream()
                .peek(p -> System.out.print(p.getFirstName()))
                .map(Person::getBirthday)
                .map(birthday -> birthday.until(now, ChronoUnit.YEARS))
                .peek(y -> System.out.println(" " + y))
                .mapToLong(Long::valueOf)
                .max()
                .orElse(-1);

    }

    public String generatePolicyData() {
        return this.policy.generatePolicyData(this.family);
    }

    public static void main(String[] strings) throws Exception {

        Policy policy = getMyPolicy().orElseGet(Policy::uninsured);

        FamilyInsurance insurance = new FamilyInsurance(policy, Family.SOME_FAMILY);
        System.out.println("Highest insured age: " + insurance.getHighestAge());

        System.out.println(insurance.generatePolicyData());

        System.out.println("Classpath: " + System.getProperty("java.class.path"));
        System.out.println("Modulepath: " + System.getProperty("jdk.module.path"));
        System.out.println("Workshop modules: ");
        if (FamilyInsurance.class.getModule().isNamed()) {
            FamilyInsurance.class.getModule().getLayer().modules()
                    .stream()
                    .map(Module::getName)
                    .filter(name -> !(name.startsWith("java") || name.startsWith("jdk")))
                    .sorted()
                    .forEach(System.out::println);
        } else {
            System.out.println("none");
        }
    }

    private static Optional<Policy> getMyPolicy() {
        try {
            return Optional.of((Policy) Class.forName("workshop.my.policy.MyPolicy").getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
