package io.hrushik09.tasker.cards;

public record CardDTO(
        Integer id,
        String title,
        Integer listId,
        String description
) {
    public static CardDTO from(Card card) {
        return new CardDTO(card.getId(), card.getTitle(), card.getList().getId(), card.getDescription());
    }
}
