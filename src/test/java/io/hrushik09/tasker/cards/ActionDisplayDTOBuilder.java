package io.hrushik09.tasker.cards;

import static io.hrushik09.tasker.cards.ActionDisplayEntitiesDTOBuilder.anActionDisplayEntitiesDTOBuilder;

public class ActionDisplayDTOBuilder {
    private String translationKey = "Not important";
    private ActionDisplayEntitiesDTOBuilder actionDisplayEntitiesDTOBuilder = anActionDisplayEntitiesDTOBuilder();

    private ActionDisplayDTOBuilder() {
    }

    private ActionDisplayDTOBuilder(ActionDisplayDTOBuilder copy) {
        this.translationKey = copy.translationKey;
        this.actionDisplayEntitiesDTOBuilder = copy.actionDisplayEntitiesDTOBuilder;
    }

    public static ActionDisplayDTOBuilder anActionDisplayDTOBuilder() {
        return new ActionDisplayDTOBuilder();
    }

    public ActionDisplayDTOBuilder but() {
        return new ActionDisplayDTOBuilder(this);
    }

    public ActionDisplayDTOBuilder withTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        return this;
    }

    public ActionDisplayDTOBuilder with(ActionDisplayEntitiesDTOBuilder actionDisplayEntitiesDTOBuilder) {
        this.actionDisplayEntitiesDTOBuilder = actionDisplayEntitiesDTOBuilder;
        return this;
    }

    public ActionDisplayDTO build() {
        return new ActionDisplayDTO(translationKey, actionDisplayEntitiesDTOBuilder.build());
    }
}
