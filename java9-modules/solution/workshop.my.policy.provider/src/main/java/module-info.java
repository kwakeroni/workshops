module workshop.my.policy.provider {

    requires workshop.insurance;
    requires static com.fasterxml.jackson.core;
    requires static com.fasterxml.jackson.annotation;
    requires static com.fasterxml.jackson.databind;
    requires static com.fasterxml.jackson.datatype.jsr310;

    opens workshop.my.policy to com.fasterxml.jackson.databind;

    provides workshop.insurance.Policy with workshop.my.policy.MyPolicy;

}