package io.hrushik09.tasker.cards;

public record UpdateCardDescriptionResponse(
        Integer id,
        String title,
        Integer listId,
        String description
) {
    public static UpdateCardDescriptionResponse from(Card card) {
        return new UpdateCardDescriptionResponse(card.getId(), card.getTitle(), card.getList().getId(), card.getDescription());
    }
}
