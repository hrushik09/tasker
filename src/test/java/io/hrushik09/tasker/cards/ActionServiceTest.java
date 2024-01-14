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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.cards.ActionBuilder.anActionBuilder;
import static io.hrushik09.tasker.cards.CardActionBuilder.aCardActionBuilder;
import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.cards.ListActionBuilder.aListActionBuilder;
import static io.hrushik09.tasker.cards.MemberCreatorActionBuilder.aMemberCreatorActionBuilder;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
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

            Mockito.verify(actionRepository).save(actionArgumentCaptor.capture());
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
            ActionBuilder actionBuilder = anActionBuilder().withId(actionId).with(aCard().withId(cardId))
                    .withMemberCreatorId(creatorId).withType("createCard").withHappenedAt(happenedAt)
                    .withTranslationKey("action_create_card")
                    .with(aCardActionBuilder().withCardId(cardId).withText(cardTitle))
                    .with(aListActionBuilder().withListId(listId).withText(listTitle))
                    .with(aMemberCreatorActionBuilder().withCreatorId(creatorId).withText(creatorName));
            when(actionRepository.findByCardId(cardId)).thenReturn(List.of(actionBuilder.build()));

            List<ActionResponse> actions = actionService.fetchAllCardActions(cardId);

            assertThat(actions).hasSize(1);
            ActionResponse action = actions.getFirst();
            assertThat(action.id()).isEqualTo(actionId);
            assertThat(action.memberCreatorId()).isEqualTo(creatorId);
            assertThat(action.type()).isEqualTo("createCard");
            assertThat(action.happenedAt()).isEqualTo(happenedAt);
            assertThat(action.display()).isNotNull();
            ActionDisplayDTO display = action.display();
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
    }
}