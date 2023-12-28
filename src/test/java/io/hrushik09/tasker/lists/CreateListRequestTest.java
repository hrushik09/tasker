package io.hrushik09.tasker.lists;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateListRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void titleShouldBeValid(String title) {
        CreateListRequest request = new CreateListRequest(title, 1);

        Set<ConstraintViolation<CreateListRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("title should be non-empty");
    }

    @Test
    void userIdShouldBeValid() {
        CreateListRequest request = new CreateListRequest("Not important", null);

        Set<ConstraintViolation<CreateListRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("userId should be non-null");
    }
}