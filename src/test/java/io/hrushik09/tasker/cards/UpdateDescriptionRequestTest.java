package io.hrushik09.tasker.cards;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateDescriptionRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void descriptionShouldBeValid(String title) {
        UpdateDescriptionRequest request = new UpdateDescriptionRequest(title);

        Set<ConstraintViolation<UpdateDescriptionRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("description should be non-empty");
    }
}