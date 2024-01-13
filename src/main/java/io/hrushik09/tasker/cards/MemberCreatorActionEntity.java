package io.hrushik09.tasker.cards;

public record MemberCreatorActionEntity(
        String type,
        Integer id,
        String text
) implements ActionEntity {
}
