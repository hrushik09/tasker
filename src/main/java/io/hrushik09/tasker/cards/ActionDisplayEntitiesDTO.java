package io.hrushik09.tasker.cards;

public record ActionDisplayEntitiesDTO(
        CardActionEntityDTO card,
        ListActionEntityDTO list,
        MemberCreatorActionEntityDTO memberCreator
) {
}
