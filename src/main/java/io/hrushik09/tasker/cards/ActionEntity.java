package io.hrushik09.tasker.cards;

public sealed interface ActionEntity permits CardActionEntity, ListActionEntity, MemberCreatorActionEntity {
}
