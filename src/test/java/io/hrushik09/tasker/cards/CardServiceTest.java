package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.boards.BoardBuilder;
import io.hrushik09.tasker.lists.ListBuilder;
import io.hrushik09.tasker.lists.ListDoesNotExistException;
import io.hrushik09.tasker.lists.ListService;
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
import java.util.Map;
import java.util.Optional;

import static io.hrushik09.tasker.boards.BoardBuilder.aBoard;
import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    private CardService cardService;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ListService listService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, listService);
    }

    @Test
    void shouldCreateCardSuccessfully() {
        Integer listId = 1;
        ListBuilder listBuilder = aList().withId(listId);
        when(listService.getReferenceById(listId)).thenReturn(listBuilder.build());
        Integer cardId = 1;
        String title = "Card 2";
        CardBuilder cardBuilder = aCard().withId(cardId).withTitle(title).with(listBuilder);
        when(cardRepository.save(any())).thenReturn(cardBuilder.build());

        CreateCardResponse created = cardService.create(new CreateCardCommand(listId, title));

        assertThat(created.id()).isEqualTo(cardId);
        assertThat(created.title()).isEqualTo(title);
        assertThat(created.listId()).isEqualTo(listId);
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());
        Card captorValue = cardArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo(title);
        assertThat(captorValue.getList().getId()).isEqualTo(listId);
    }

    @Test
    void shouldFetchAllCardsForGivenBoard() {
        Integer boardId = 1;
        List<CardMinDetailsDTO> cards = List.of(
                new CardMinDetailsDTO(1, "Card 1", 1),
                new CardMinDetailsDTO(2, "Card 2", 2),
                new CardMinDetailsDTO(3, "Card 3", 1),
                new CardMinDetailsDTO(4, "Card 4", 2),
                new CardMinDetailsDTO(5, "Card 5", 3)
        );
        when(cardRepository.fetchForAll(boardId)).thenReturn(cards);

        AllCardMinDetailsDTO fetched = cardService.fetchAllFor(boardId);

        assertThat(fetched.cards()).hasSize(5);
        assertThat(fetched.cards()).extracting("id")
                .containsExactly(1, 2, 3, 4, 5);
        assertThat(fetched.cards()).extracting("title")
                .containsExactly("Card 1", "Card 2", "Card 3", "Card 4", "Card 5");
        assertThat(fetched.cards()).extracting("listId")
                .containsExactly(1, 2, 1, 2, 3);
    }

    @Nested
    class FetchCardDetails {
        @Test
        void shouldThrowWhenFetchingCardDetailsForNonExistingCard() {
            Integer nonExistingId = 102;
            when(cardRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cardService.fetchCardDetails(nonExistingId))
                    .isInstanceOf(CardDoesNotExistException.class)
                    .hasMessage("Card with id=" + nonExistingId + " does not exist");
        }

        @Test
        void shouldFetchCardDetailsSuccessfully() {
            Integer id = 1;
            String title = "Card 1";
            String description = "current description for card 1";
            Integer listId = 3;
            String startStr = "2023-04-05T04:05:02Z";
            String dueStr = "2023-04-06T04:05:02Z";
            ListBuilder listBuilder = aList().withId(listId);
            CardBuilder cardBuilder = aCard().withId(id).withTitle(title)
                    .withDescription(description).with(listBuilder).withStart(Instant.parse(startStr))
                    .withDue(Instant.parse(dueStr));
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));

            CardMaxDetailsDTO fetched = cardService.fetchCardDetails(id);
            assertThat(fetched.id()).isEqualTo(id);
            assertThat(fetched.title()).isEqualTo(title);
            assertThat(fetched.description()).isEqualTo(description);
            assertThat(fetched.start()).isEqualTo(Instant.parse(startStr));
            assertThat(fetched.due()).isEqualTo(Instant.parse(dueStr));
            assertThat(fetched.listId()).isEqualTo(listId);
            assertThat(fetched.createdAt()).isNotNull();
            assertThat(fetched.updatedAt()).isNotNull();
        }
    }

    @Nested
    class UpdateCard {
        @Captor
        ArgumentCaptor<Card> cardArgumentCaptor;

        @Test
        void shouldThrowWhenUpdatingNonExistingCard() {
            Integer nonExistingId = 1;
            Map<String, Object> fields = Map.of("description", "Not important");

            assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(nonExistingId, fields)))
                    .isInstanceOf(CardDoesNotExistException.class)
                    .hasMessage("Card with id=" + nonExistingId + " does not exist");
        }

        @Test
        void shouldThrowWhenUpdatingNonExistingField() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withDescription(null);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            Map<String, Object> fields = Map.of("invalidFieldName", "Not important");

            assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(id, fields)))
                    .isInstanceOf(InvalidFieldForUpdateCardException.class)
                    .hasMessage("Field invalidFieldName not found in Card");
        }

        @Test
        void shouldReturnCorrectResponseFieldsAfterAllowedFieldUpdateIsPerformed() {
            Integer id = 2;
            ListBuilder listBuilder = aList().withId(11);
            CardBuilder cardBuilder = aCard().withId(id).withTitle("Documentation").withDescription(null).with(listBuilder);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            String updatedDescription = "Not important";
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withDescription(updatedDescription).build());
            Map<String, Object> fields = Map.of("description", updatedDescription);

            UpdateCardResponse updated = cardService.update(new UpdateCardCommand(id, fields));

            assertThat(updated.id()).isEqualTo(id);
            assertThat(updated.title()).isEqualTo("Documentation");
            assertThat(updated.listId()).isEqualTo(11);
        }

        @Test
        void shouldUpdateTitleSuccessfully() {
            Integer id = 1;
            String originalTitle = "Original title";
            CardBuilder cardBuilder = aCard().withId(id).withTitle(originalTitle);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            String updatedTitle = "Title after update";
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withTitle(updatedTitle).build());
            Map<String, Object> fields = Map.of("title", updatedTitle);

            UpdateCardResponse updated = cardService.update(new UpdateCardCommand(id, fields));

            assertThat(updated.title()).isEqualTo(updatedTitle);
            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.getTitle()).isEqualTo(updatedTitle);
        }

        @Test
        void shouldUpdateDescriptionSuccessfully() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withDescription(null);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            String updatedDescription = "This is updated description";
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withDescription(updatedDescription).build());
            Map<String, Object> fields = Map.of("description", updatedDescription);

            cardService.update(new UpdateCardCommand(id, fields));

            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.getDescription()).isEqualTo(updatedDescription);
        }

        @Test
        void shouldUpdateStartSuccessfully() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withStart(null);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            String startStr = "2023-04-24T13:45:55Z";
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withStart(Instant.parse(startStr)).build());
            Map<String, Object> fields = Map.of("start", startStr);

            cardService.update(new UpdateCardCommand(id, fields));

            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.getStart()).isEqualTo(Instant.parse(startStr));
        }

        @Test
        void shouldUpdateDueSuccessfully() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withDue(null);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            String dueStr = "2023-04-25T13:45:55Z";
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withDue(Instant.parse(dueStr)).build());
            Map<String, Object> fields = Map.of("due", dueStr);

            cardService.update(new UpdateCardCommand(id, fields));

            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.getDue()).isEqualTo(Instant.parse(dueStr));
        }

        @Test
        void shouldArchiveCardSuccessfully() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withArchived(false);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withArchived(true).build());
            Map<String, Object> fields = Map.of("archived", true);

            cardService.update(new UpdateCardCommand(id, fields));

            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.isArchived()).isTrue();
        }

        @Test
        void shouldUnarchiveCardSuccessfully() {
            Integer id = 1;
            CardBuilder cardBuilder = aCard().withId(id).withArchived(true);
            when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
            when(cardRepository.save(any())).thenReturn(cardBuilder.but().withArchived(false).build());
            Map<String, Object> fields = Map.of("archived", false);

            cardService.update(new UpdateCardCommand(id, fields));

            verify(cardRepository).save(cardArgumentCaptor.capture());
            Card captorValue = cardArgumentCaptor.getValue();
            assertThat(captorValue.isArchived()).isFalse();
        }

        @Nested
        class MoveCardToAnotherList {
            @Test
            void shouldThrowWhenMovingCardToNonExistingList() {
                Integer nonExistingListId = 12;
                ListBuilder listBuilder = aList().withId(nonExistingListId);
                Integer id = 1;
                CardBuilder cardBuilder = aCard().withId(id).with(listBuilder);
                when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
                when(listService.findById(nonExistingListId)).thenThrow(new ListDoesNotExistException(nonExistingListId));
                Map<String, Object> fields = Map.of("list", nonExistingListId);

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(id, fields)))
                        .isInstanceOf(ListDoesNotExistException.class)
                        .hasMessage("List with id=12 does not exist");
            }

            @Test
            void shouldThrowWhenMovingCardToListNotInCurrentBoard() {
                Integer originalBoardId = 1;
                BoardBuilder originalBoardBuilder = aBoard().withId(originalBoardId);
                Integer originalListId = 12;
                ListBuilder originalListBuilder = aList().withId(originalListId).with(originalBoardBuilder);
                Integer id = 1;
                CardBuilder cardBuilder = aCard().withId(id).with(originalListBuilder);
                when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
                Integer newBoardId = 2;
                BoardBuilder newBoardBuilder = originalBoardBuilder.but().withId(newBoardId);
                Integer invalidListId = 100;
                ListBuilder invalidListBuilder = originalListBuilder.but().withId(invalidListId).with(newBoardBuilder);
                when(listService.findById(invalidListId)).thenReturn(invalidListBuilder.build());
                Map<String, Object> fields = Map.of("list", invalidListId);

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(id, fields)))
                        .isInstanceOf(ListNotInGivenBoardException.class)
                        .hasMessage("List with id=" + invalidListId + " does not exist in current board");
            }

            @Test
            void shouldMoveCardToDifferentListSuccessfully() {
                Integer boardId = 23;
                BoardBuilder boardBuilder = aBoard().withId(boardId);
                Integer originalListId = 43;
                ListBuilder listBuilder = aList().withId(originalListId).with(boardBuilder);
                Integer id = 1;
                CardBuilder cardBuilder = aCard().withId(id).with(listBuilder);
                when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
                Integer newListId = 2;
                ListBuilder newListBuilder = listBuilder.but().withId(newListId);
                when(listService.findById(newListId)).thenReturn(newListBuilder.build());
                CardBuilder newCardBuilder = cardBuilder.but().with(newListBuilder);
                when(cardRepository.save(any())).thenReturn(newCardBuilder.build());
                Map<String, Object> fields = Map.of("list", newListId);

                UpdateCardResponse updated = cardService.update(new UpdateCardCommand(id, fields));

                verify(cardRepository).save(cardArgumentCaptor.capture());
                Card captorValue = cardArgumentCaptor.getValue();
                assertThat(captorValue.getList().getId()).isEqualTo(newListId);
                assertThat(updated.listId()).isEqualTo(newListId);
            }
        }

        @Nested
        class NotAllowedFieldsToUpdate {
            @Test
            void shouldThrowWhenUpdatingFieldId() {
                Map<String, Object> fields = Map.of("id", 100);

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(1, fields)))
                        .isInstanceOf(NotAllowedFieldForUpdateCardException.class)
                        .hasMessage("Field id is not allowed for update");
            }

            @Test
            void shouldThrowWhenUpdatingFieldList() {
                Map<String, Object> fields = Map.of("list", "Not important");

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(1, fields)))
                        .isInstanceOf(NotAllowedFieldForUpdateCardException.class)
                        .hasMessage("Field list is not allowed for update");
            }

            @Test
            void shouldThrowWhenUpdatingFieldCreatedAt() {
                Map<String, Object> fields = Map.of("createdAt", "Not important");

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(1, fields)))
                        .isInstanceOf(NotAllowedFieldForUpdateCardException.class)
                        .hasMessage("Field createdAt is not allowed for update");
            }

            @Test
            void shouldThrowWhenUpdatingFieldIUpdatedAt() {
                Map<String, Object> fields = Map.of("updatedAt", "Not important");

                assertThatThrownBy(() -> cardService.update(new UpdateCardCommand(1, fields)))
                        .isInstanceOf(NotAllowedFieldForUpdateCardException.class)
                        .hasMessage("Field updatedAt is not allowed for update");
            }
        }
    }
}