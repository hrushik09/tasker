package io.hrushik09.tasker.cards;

public record ListActionDTO(
        String type,
        Integer id,
        String text
) implements TypeOfActionDTO {
}
