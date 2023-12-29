package io.hrushik09.tasker.boards;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateBoardRequestTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @NullAndEmptySource
    void titleShouldBeValid(String title) {
        CreateBoardRequest request = new CreateBoardRequest(title);

        Set<ConstraintViolation<CreateBoardRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations).extracting("message").containsExactly("title should be non-empty");
    }
}