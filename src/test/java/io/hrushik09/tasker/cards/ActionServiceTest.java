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

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static io.hrushik09.tasker.users.UserBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;

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
}