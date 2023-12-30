package io.hrushik09.tasker.cards;

import jakarta.validation.constraints.NotEmpty;

public record CreateCardRequest(
        @NotEmpty(message = "title should be non-empty")
        String title
) {
}
