package io.hrushik09.tasker.cards;

public sealed interface TypeOfActionDTO permits CardActionDTO, DateActionDTO, ListActionDTO, MemberCreatorActionDTO {
}
