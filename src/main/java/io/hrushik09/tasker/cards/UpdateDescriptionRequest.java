package io.hrushik09.tasker.cards;

import jakarta.validation.constraints.NotEmpty;

public record UpdateDescriptionRequest(
        @NotEmpty(message = "description should be non-empty")
        String description
) {
}
