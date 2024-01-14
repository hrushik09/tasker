package io.hrushik09.tasker.cards;

public record CardActionEntityDTO(
        String type,
        Integer id,
        String text
) implements ActionEntityDTO {
}
