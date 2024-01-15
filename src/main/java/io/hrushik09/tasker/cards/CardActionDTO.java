package io.hrushik09.tasker.cards;

import java.time.Instant;

public record CardActionDTO(
        String type,
        Integer id,
        String text,
        Instant due) implements TypeOfActionDTO {
}
