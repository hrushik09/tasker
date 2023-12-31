package io.hrushik09.tasker.cards;

public record UpdateDescriptionCommand(
        Integer id,
        String description
) {
}
