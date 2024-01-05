package io.hrushik09.tasker.cards;

public record CreateCardResponse(
        Integer id,
        String title,
        Integer listId
) {
    public static CreateCardResponse from(Card card) {
        return new CreateCardResponse(card.getId(), card.getTitle(), card.getList().getId());
    }
}
