package io.hrushik09.tasker.lists;

import jakarta.validation.constraints.NotEmpty;

public record UpdateListRequest(
        @NotEmpty(message = "title should be non-empty")
        String title
) {
}
