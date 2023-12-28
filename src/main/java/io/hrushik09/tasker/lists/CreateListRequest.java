package io.hrushik09.tasker.lists;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateListRequest(
        @NotEmpty(message = "title should be non-empty")
        String title,
        @NotNull(message = "userId should be non-null")
        Integer userId
) {
}
