package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.boards.BoardBuilder;
import io.hrushik09.tasker.lists.ListBuilder;
import io.hrushik09.tasker.users.UserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.cards.ActionBuilder.anActionBuilder;
import static io.hrushik09.tasker.cards.CardActionBuilder.aCardActionBuilder;
import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.cards.DateActionBuilder.aDateActionBuilder;
import static io.hrushik09.tasker.cards.ListActionBuilder.aListActionBuilder;
import static io.hrushik09.tasker.cards.MemberCreatorActionBuilder.aMemberCreatorActionBuilder;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {
    private ActionService actionService;
    @Mock
    private ActionRepository actionRepository;

    @BeforeEach
    void setUp() {
        actionService = new ActionService(actionRepository);
    }

    @Nested
    class AddCardActionDetails {
        @Captor
        ArgumentCaptor<Action> actionArgumentCaptor;

        @Test
        void shouldAddCreateCardAction() {
            Integer creatorId = 23;
            String creatorName = "User 4";
            UserBuilder userBuilder = aUser().withId(creatorId).withName(creatorName);
            BoardBuilder boardBuilder = aBoard().with(userBuilder);
            Integer listId = 1;
            String listTitle = "Completed";
            ListBuilder listBuilder = aList().withId(listId).withTitle(listTitle).with(boardBuilder);
            Integer cardId = 4;
            String cardTitle = "Documentation";
            CardBuilder cardBuilder = aCard().withId(cardId).withTitle(cardTitle).with(listBuilder);

            actionService.saveCreateCardAction(cardBuilder.build());

            verify(actionRepository).save(actionArgumentCaptor.capture());
            Action captorValue = actionArgumentCaptor.getValue();
            assertThat(captorValue.getCard().getId()).isEqualTo(cardId);
            assertThat(captorValue.getMemberCreatorId()).isEqualTo(creatorId);
            assertThat(captorValue.getType()).isEqualTo("createCard");
            assertThat(captorValue.getHappenedAt()).isNotNull();
            assertThat(captorValue.getTranslationKey()).isEqualTo("action_create_card");
            assertThat(captorValue.getCardAction()).isNotNull();
            assertThat(captorValue.getCardAction().getType()).isEqualTo("card");
            assertThat(captorValue.getCardAction().getCardId()).isEqualTo(cardId);
            assertThat(captorValue.getCardAction().getText()).isEqualTo(cardTitle);
            assertThat(captorValue.getListAction()).isNotNull();
            assertThat(captorValue.getListAction().getType()).isEqualTo("list");
            assertThat(captorValue.getListAction().getListId()).isEqualTo(listId);
            assertThat(captorValue.getListAction().getText()).isEqualTo(listTitle);
            assertThat(captorValue.getMemberCreatorAction()).isNotNull();
            assertThat(captorValue.getMemberCreatorAction().getType()).isEqualTo("member");
            assertThat(captorValue.getMemberCreatorAction().getCreatorId()).isEqualTo(creatorId);
            assertThat(captorValue.getMemberCreatorAction().getText()).isEqualTo(creatorName);
        }

        @Test
        void shouldAddDueAction() {
            Integer creatorId = 98;
            String creatorName = "User abc";
            UserBuilder userBuilder = aUser().withId(creatorId).withName(creatorName);
            BoardBuilder boardBuilder = aBoard().with(userBuilder);
            ListBuilder listBuilder = aList().with(boardBuilder);
            Integer cardId = 45;
            String cardTitle = "This is a card title";
            String dueStr = "2023-12-12T23:23:23Z";
            CardBuilder cardBuilder = aCard().withId(cardId).withTitle(cardTitle).withDue(Instant.parse(dueStr))
                    .with(listBuilder);

            actionService.saveAddDueAction(cardBuilder.build());

            verify(actionRepository).save(actionArgumentCaptor.capture());
            Action captorValue = actionArgumentCaptor.getValue();
            assertThat(captorValue.getCard().getId()).isEqualTo(cardId);
            assertThat(captorValue.getMemberCreatorId()).isEqualTo(creatorId);
            assertThat(captorValue.getType()).isEqualTo("updateCard");
            assertThat(captorValue.getHappenedAt()).isNotNull();
            assertThat(captorValue.getTranslationKey()).isEqualTo("action_added_a_due_date");
            assertThat(captorValue.getCardAction()).isNotNull();
            assertThat(captorValue.getCardAction().getType()).isEqualTo("card");
            assertThat(captorValue.getCardAction().getCardId()).isEqualTo(cardId);
            assertThat(captorValue.getCardAction().getText()).isEqualTo(cardTitle);
            assertThat(captorValue.getCardAction().getDue()).isEqualTo(Instant.parse(dueStr));
            assertThat(captorValue.getDateAction()).isNotNull();
            assertThat(captorValue.getDateAction().getType()).isEqualTo("date");
            assertThat(captorValue.getDateAction().getDueAt()).isEqualTo(Instant.parse(dueStr));
            assertThat(captorValue.getMemberCreatorAction()).isNotNull();
            assertThat(captorValue.getMemberCreatorAction().getType()).isEqualTo("member");
            assertThat(captorValue.getMemberCreatorAction().getCreatorId()).isEqualTo(creatorId);
            assertThat(captorValue.getMemberCreatorAction().getText()).isEqualTo(creatorName);
        }
    }

    @Nested
    class FetchCardActionDetails {
        @Test
        void shouldFetchCreateCardActionDetails() {
            Integer creatorId = 11;
            String creatorName = "User 10";
            Integer listId = 2;
            String listTitle = "Future tasks";
            Integer cardId = 2;
            String cardTitle = "New feature";
            Integer actionId = 3;
            Instant happenedAt = Instant.parse("2023-12-12T01:01:01Z");
            ActionBuilder createActionBuilder = anActionBuilder().withId(actionId).with(aCard().withId(cardId))
                    .withMemberCreatorId(creatorId).withType("createCard").withHappenedAt(happenedAt)
                    .withTranslationKey("action_create_card")
                    .with(aCardActionBuilder().withCardId(cardId).withText(cardTitle))
                    .with(aListActionBuilder().withListId(listId).withText(listTitle))
                    .with(aMemberCreatorActionBuilder().withCreatorId(creatorId).withText(creatorName));
            when(actionRepository.findByCardId(cardId)).thenReturn(List.of(createActionBuilder.build()));

            List<ActionResponse> actions = actionService.fetchAllCardActions(cardId);

            assertThat(actions).hasSize(1);
            ActionResponse actionResponse = actions.getFirst();
            assertThat(actionResponse.id()).isEqualTo(actionId);
            assertThat(actionResponse.memberCreatorId()).isEqualTo(creatorId);
            assertThat(actionResponse.type()).isEqualTo("createCard");
            assertThat(actionResponse.happenedAt()).isEqualTo(happenedAt);
            assertThat(actionResponse.display()).isNotNull();
            ActionDisplayDTO display = actionResponse.display();
            assertThat(display.translationKey()).isEqualTo("action_create_card");
            assertThat(display.entities()).isNotNull();
            ActionDisplayEntitiesDTO entities = display.entities();
            assertThat(entities.card()).isNotNull();
            assertThat(entities.card().type()).isEqualTo("card");
            assertThat(entities.card().id()).isEqualTo(cardId);
            assertThat(entities.card().text()).isEqualTo(cardTitle);
            assertThat(entities.list()).isNotNull();
            assertThat(entities.list().type()).isEqualTo("list");
            assertThat(entities.list().id()).isEqualTo(listId);
            assertThat(entities.list().text()).isEqualTo(listTitle);
            assertThat(entities.memberCreator()).isNotNull();
            assertThat(entities.memberCreator().type()).isEqualTo("member");
            assertThat(entities.memberCreator().id()).isEqualTo(creatorId);
            assertThat(entities.memberCreator().text()).isEqualTo(creatorName);
        }

        @Test
        void shouldFetchAddDueActionDetails() {
            ActionBuilder createActionBuilder = anActionBuilder().withType("createCard");
            Integer cardId = 7;
            String cardTitle = "Add Spring Security";
            Integer actionId = 98;
            Integer creatorId = 56;
            Instant happenedAt = Instant.parse("2023-01-23T09:09:09Z");
            Instant due = Instant.parse("2023-12-09T21:12:05Z");
            String creatorName = "User 1";
            ActionBuilder addDueActionBuilder = anActionBuilder().withId(actionId).with(aCard().withId(cardId))
                    .withMemberCreatorId(creatorId).withType("updateCard").withHappenedAt(happenedAt)
                    .withTranslationKey("action_added_a_due_date")
                    .with(aCardActionBuilder().withCardId(cardId).withText(cardTitle).withDue(due))
                    .with(aDateActionBuilder().withDueAt(due))
                    .with(aMemberCreatorActionBuilder().withCreatorId(creatorId).withText(creatorName));
            when(actionRepository.findByCardId(cardId)).thenReturn(List.of(createActionBuilder.build(), addDueActionBuilder.build()));

            List<ActionResponse> actions = actionService.fetchAllCardActions(cardId);

            assertThat(actions).hasSize(2);
            ActionResponse actionResponse = actions.get(1);
            assertThat(actionResponse.id()).isEqualTo(actionId);
            assertThat(actionResponse.memberCreatorId()).isEqualTo(creatorId);
            assertThat(actionResponse.type()).isEqualTo("updateCard");
            assertThat(actionResponse.happenedAt()).isEqualTo(happenedAt);
            assertThat(actionResponse.display()).isNotNull();
            ActionDisplayDTO display = actionResponse.display();
            assertThat(display.translationKey()).isEqualTo("action_added_a_due_date");
            assertThat(display.entities()).isNotNull();
            ActionDisplayEntitiesDTO entities = display.entities();
            assertThat(entities.card()).isNotNull();
            assertThat(entities.card().type()).isEqualTo("card");
            assertThat(entities.card().id()).isEqualTo(cardId);
            assertThat(entities.card().text()).isEqualTo(cardTitle);
            assertThat(entities.card().due()).isEqualTo(due);
            assertThat(entities.date()).isNotNull();
            assertThat(entities.date().type()).isEqualTo("date");
            assertThat(entities.date().date()).isEqualTo(due);
            assertThat(entities.memberCreator()).isNotNull();
            assertThat(entities.memberCreator().type()).isEqualTo("member");
            assertThat(entities.memberCreator().id()).isEqualTo(creatorId);
            assertThat(entities.memberCreator().text()).isEqualTo(creatorName);
        }
    }
}