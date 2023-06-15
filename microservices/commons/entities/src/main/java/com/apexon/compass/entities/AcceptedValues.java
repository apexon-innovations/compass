package com.apexon.compass.entities;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ PARAMETER, FIELD, ANNOTATION_TYPE, METHOD })
@Retention(RUNTIME)
@Constraint(validatedBy = { AcceptedValues.AcceptedValuesValidator.class })
public @interface AcceptedValues {

    String message() default "Not allowed to enter this value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};

    public class AcceptedValuesValidator implements ConstraintValidator<AcceptedValues, String> {

        private String[] validValues;

        @Override
        public void initialize(AcceptedValues context) {
            this.validValues = context.value();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            Set<String> validValuesSet = new HashSet<String>();
            validValuesSet.addAll(Arrays.asList(validValues));
            return Objects.isNull(value) || validValuesSet.contains(value);
        }

    }

}
