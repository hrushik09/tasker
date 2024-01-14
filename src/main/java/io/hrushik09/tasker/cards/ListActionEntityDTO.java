package io.hrushik09.tasker.cards;

public record ListActionEntityDTO(
        String type,
        Integer id,
        String text
) implements ActionEntityDTO {
}
