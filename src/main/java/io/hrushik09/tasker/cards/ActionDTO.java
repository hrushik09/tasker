package io.hrushik09.tasker.cards;

import java.time.Instant;

public record ActionDTO(
        Integer id,
        Integer memberCreatorId,
        String type,
        Instant happenedAt,
        ActionDisplayDTO display
) {
}
