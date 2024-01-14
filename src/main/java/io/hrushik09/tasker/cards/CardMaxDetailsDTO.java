package io.hrushik09.tasker.cards;

import java.time.Instant;
import java.util.List;

public record CardMaxDetailsDTO(
        Integer id,
        String title,
        String description,
        Instant start,
        Instant due,
        Integer listId,
        Instant createdAt,
        Instant updatedAt,
        List<ActionDTO> actions
) {
    public static CardMaxDetailsDTO from(Card card, List<ActionDTO> actions) {
        return new CardMaxDetailsDTO(card.getId(), card.getTitle(), card.getDescription(), card.getStart(),
                card.getDue(), card.getList().getId(), card.getCreatedAt(), card.getUpdatedAt(), actions);
    }
}
