package io.hrushik09.tasker.cards;

import java.time.Instant;

public record ActionResponse(
        Integer id,
        Integer memberCreatorId,
        String type,
        Instant happenedAt,
        ActionDisplayDTO display
) {
    public static ActionResponse from(Action action) {
        DateActionDTO dateActionDTO;
        if (action.getDateAction() != null) {
            dateActionDTO = new DateActionDTO(action.getDateAction().getType(), action.getDateAction().getDueAt());
        } else {
            dateActionDTO = null;
        }
        Instant due;
        if (action.getCardAction().getDue() != null) {
            due = action.getCardAction().getDue();
        } else {
            due = null;
        }
        ListActionDTO listActionDTO;
        if (action.getListAction() != null) {
            listActionDTO = new ListActionDTO(action.getListAction().getType(), action.getListAction().getListId(), action.getListAction().getText());
        } else {
            listActionDTO = null;
        }
        return new ActionResponse(action.getId(), action.getMemberCreatorId(), action.getType(), action.getHappenedAt(),
                new ActionDisplayDTO(action.getTranslationKey(),
                        new ActionDisplayEntitiesDTO(
                                new CardActionDTO(action.getCardAction().getType(), action.getCardAction().getCardId(), action.getCardAction().getText(), due),
                                listActionDTO,
                                new MemberCreatorActionDTO(action.getMemberCreatorAction().getType(), action.getMemberCreatorAction().getCreatorId(), action.getMemberCreatorAction().getText()),
                                dateActionDTO)
                )
        );
    }
}
