package io.hrushik09.tasker.cards;

import java.time.Instant;

public record DateActionDTO(
        String type,
        Instant date
) implements TypeOfActionDTO {
}
