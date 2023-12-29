package io.hrushik09.tasker.boards;

import jakarta.validation.constraints.NotEmpty;

public record CreateBoardRequest(
        @NotEmpty(message = "title should be non-empty")
        String title
) {
}
