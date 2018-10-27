package workshop.my.policy;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import workshop.family.Family;
import workshop.insurance.Policy;

import java.io.UncheckedIOException;

public class MyPolicy implements Policy {

    @Override
    public String generatePolicyData(Family family) {
        if (isClassDefined("com.fasterxml.jackson.databind.ObjectMapper")) {
            return JacksonMapper.toStringJackson(family);
        } else {
            return family.toString();
        }
    }

    public static class FamilyWrapper {
        public final Family insuredFamily;

        public FamilyWrapper(Family insuredFamily) {
            this.insuredFamily = insuredFamily;
        }
    }

    private static boolean isClassDefined(String name) {
        try {
            return Class.forName(name) != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static class JacksonMapper {
        private static String toStringJackson(Family family) {
            ObjectMapper mapper = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .setVisibility(PropertyAccessor.ALL, Visibility.NONE)
                    .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
                    .enable(SerializationFeature.INDENT_OUTPUT);

            try {
                return mapper.writeValueAsString(new FamilyWrapper(family));
            } catch (JsonProcessingException exc) {
                throw new UncheckedIOException(exc);
            }
        }
    }
}
