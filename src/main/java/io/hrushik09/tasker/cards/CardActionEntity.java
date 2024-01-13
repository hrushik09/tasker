package io.hrushik09.tasker.cards;

public record CardActionEntity(
        String type,
        Integer id,
        String text
) implements ActionEntity {
}
