package io.hrushik09.tasker.cards;

import java.util.Map;

public record UpdateCardCommand(
        Integer id,
        Map<String, Object> fields
) {
}
