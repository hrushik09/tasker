package io.hrushik09.tasker.cards;

import java.time.Instant;

public record Action(
        Integer id,
        Integer memberCreatorId,
        String type,
        Instant happenedAt,
        ActionDisplay display
) {
}
