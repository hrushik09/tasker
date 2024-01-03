package io.hrushik09.tasker.cards;

public record CardMinDTO(
        Integer id,
        String title,
        Integer listId
) {
    public static CardMinDTO from(Card card) {
        return new CardMinDTO(card.getId(), card.getTitle(), card.getList().getId());
    }
}
