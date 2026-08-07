package com.company.notification.validation;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AtLeastOneFieldPresentValidatorTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        factory.close();
    }

    @Test
    void rejectsCompletelyEmptyPatch() {
        var request = new NotificationPreferencesPatchRequest(null, null, null);

        Set<?> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void acceptsPatchWithAtLeastOneField() {
        var request = new NotificationPreferencesPatchRequest(null, true, null);

        Set<?> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }
}
