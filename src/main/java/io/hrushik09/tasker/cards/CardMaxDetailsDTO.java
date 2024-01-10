package io.hrushik09.tasker.cards;

import java.time.Instant;

public record CardMaxDetailsDTO(
        Integer id,
        String title,
        String description,
        String start,
        String due,
        Integer listId,
        Instant createdAt,
        Instant updatedAt
) {
    public static CardMaxDetailsDTO from(Card card) {
        return new CardMaxDetailsDTO(card.getId(), card.getTitle(), card.getDescription(), card.getStart().toString(),
                card.getDue().toString(), card.getList().getId(), card.getCreatedAt(), card.getUpdatedAt());
    }
}
