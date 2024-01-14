package io.hrushik09.tasker.cards;

public record MemberCreatorActionEntityDTO(
        String type,
        Integer id,
        String text
) implements ActionEntityDTO {
}
