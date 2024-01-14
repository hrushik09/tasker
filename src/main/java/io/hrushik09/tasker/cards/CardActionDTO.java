package io.hrushik09.tasker.cards;

public record CardActionDTO(
        String type,
        Integer id,
        String text
) implements TypeOfActionDTO {
}
