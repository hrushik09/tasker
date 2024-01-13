package io.hrushik09.tasker.cards;

public record ListActionEntity(
        String type,
        Integer id,
        String text
) implements ActionEntity {
}
