package io.hrushik09.tasker.cards;

import static io.hrushik09.tasker.cards.CardActionDTOBuilder.aCardActionDTO;
import static io.hrushik09.tasker.cards.ListActionDTOBuilder.aListActionDTO;
import static io.hrushik09.tasker.cards.MemberCreatorActionDTOBuilder.aMemberCreatorActionDTO;

public class ActionDisplayEntitiesDTOBuilder {
    private CardActionDTOBuilder cardActionDTOBuilder = aCardActionDTO();
    private ListActionDTOBuilder listActionDTOBuilder = aListActionDTO();
    private MemberCreatorActionDTOBuilder memberCreatorActionDTOBuilder = aMemberCreatorActionDTO();

    private ActionDisplayEntitiesDTOBuilder() {
    }

    private ActionDisplayEntitiesDTOBuilder(ActionDisplayEntitiesDTOBuilder copy) {
        this.cardActionDTOBuilder = copy.cardActionDTOBuilder;
        this.listActionDTOBuilder = copy.listActionDTOBuilder;
        this.memberCreatorActionDTOBuilder = copy.memberCreatorActionDTOBuilder;
    }

    public static ActionDisplayEntitiesDTOBuilder anActionDisplayEntitiesDTOBuilder() {
        return new ActionDisplayEntitiesDTOBuilder();
    }

    public ActionDisplayEntitiesDTOBuilder but() {
        return new ActionDisplayEntitiesDTOBuilder(this);
    }

    public ActionDisplayEntitiesDTOBuilder with(CardActionDTOBuilder cardActionDTOBuilder) {
        this.cardActionDTOBuilder = cardActionDTOBuilder;
        return this;
    }

    public ActionDisplayEntitiesDTOBuilder with(ListActionDTOBuilder listActionDTOBuilder) {
        this.listActionDTOBuilder = listActionDTOBuilder;
        return this;
    }

    public ActionDisplayEntitiesDTOBuilder with(MemberCreatorActionDTOBuilder memberCreatorActionDTOBuilder) {
        this.memberCreatorActionDTOBuilder = memberCreatorActionDTOBuilder;
        return this;
    }

    public ActionDisplayEntitiesDTO build() {
        return new ActionDisplayEntitiesDTO(cardActionDTOBuilder.build(), listActionDTOBuilder.build(), memberCreatorActionDTOBuilder.build());
    }
}
