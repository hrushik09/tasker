package io.hrushik09.tasker.cards;

public record MemberCreatorActionDTO(
        String type,
        Integer id,
        String text
) implements TypeOfActionDTO {
}
