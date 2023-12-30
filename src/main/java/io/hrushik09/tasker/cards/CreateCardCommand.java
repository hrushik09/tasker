package io.hrushik09.tasker.cards;

public record CreateCardCommand(
        Integer listId,
        String title
) {
}
