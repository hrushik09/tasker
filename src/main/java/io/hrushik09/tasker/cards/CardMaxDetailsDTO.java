package io.hrushik09.tasker.cards;

public record CardMaxDetailsDTO(
        Integer id,
        String title,
        String description
) {
    public static CardMaxDetailsDTO from(Card card) {
        return new CardMaxDetailsDTO(card.getId(), card.getTitle(), card.getDescription());
    }
}
