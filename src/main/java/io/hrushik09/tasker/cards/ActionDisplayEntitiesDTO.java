package io.hrushik09.tasker.cards;

public record ActionDisplayEntitiesDTO(
        CardActionDTO card,
        ListActionDTO list,
        MemberCreatorActionDTO memberCreator
) {
}
