package io.hrushik09.tasker.lists;

import jakarta.validation.constraints.NotEmpty;

public record CreateListRequest(
        @NotEmpty(message = "title should be non-empty")
        String title
) {
}
