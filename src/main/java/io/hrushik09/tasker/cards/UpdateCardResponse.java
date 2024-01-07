package io.hrushik09.tasker.cards;

public record UpdateCardResponse(
        Integer id,
        String title,
        Integer listId
) {
    public static UpdateCardResponse from(Card card) {
        return new UpdateCardResponse(card.getId(), card.getTitle(), card.getList().getId());
    }
}
