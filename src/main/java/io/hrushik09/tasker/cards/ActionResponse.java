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
        return new ActionResponse(action.getId(), action.getMemberCreatorId(), action.getType(), action.getHappenedAt(),
                new ActionDisplayDTO(action.getTranslationKey(),
                        new ActionDisplayEntitiesDTO(
                                new CardActionDTO(action.getCardAction().getType(), action.getCardAction().getCardId(), action.getCardAction().getText()),
                                new ListActionDTO(action.getListAction().getType(), action.getListAction().getListId(), action.getListAction().getText()),
                                new MemberCreatorActionDTO(action.getMemberCreatorAction().getType(), action.getMemberCreatorAction().getCreatorId(), action.getMemberCreatorAction().getText())
                        )
                )
        );
    }
}
