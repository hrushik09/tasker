package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListBuilder;
import io.hrushik09.tasker.lists.ListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ListService listService;
    private CardService cardService;

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
        Card card = aCard().withId(cardId).withTitle(title).with(listBuilder).build();
        when(cardRepository.save(any())).thenReturn(card);

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
    void shouldThrowWhenUpdatingDescriptionForNonExistingCard() {
        Integer nonExistingId = 1;
        assertThatThrownBy(() -> cardService.updateDescription(new UpdateDescriptionCommand(nonExistingId, "Not important")))
                .isInstanceOf(CardDoesNotExistException.class)
                .hasMessage("Card with id=" + nonExistingId + " does not exist");
    }

    @Test
    void shouldUpdateDescriptionSuccessfully() {
        Integer id = 1;
        String updatedDescription = "This is updated description";
        CardBuilder cardBuilder = aCard().withId(id).withDescription(null);
        when(cardRepository.findById(id)).thenReturn(Optional.of(cardBuilder.build()));
        when(cardRepository.save(any())).thenReturn(cardBuilder.but().withDescription(updatedDescription).build());

        UpdateCardDescriptionResponse updated = cardService.updateDescription(new UpdateDescriptionCommand(id, updatedDescription));

        assertThat(updated.description()).isEqualTo(updatedDescription);
        assertThat(updated.id()).isEqualTo(id);
        assertThat(updated.title()).isNotNull();
        assertThat(updated.listId()).isNotNull();
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());
        Card captorValue = cardArgumentCaptor.getValue();
        assertThat(captorValue.getDescription()).isEqualTo(updatedDescription);
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
        assertThat(fetched.cards()).extracting("id").containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        assertThat(fetched.cards()).extracting("title").containsExactlyInAnyOrder("Card 1", "Card 2", "Card 3", "Card 4", "Card 5");
        assertThat(fetched.cards()).extracting("listId").containsExactlyInAnyOrder(1, 2, 1, 2, 3);
    }

    @Test
    void shouldThrowWhenFetchingCardDetailsForNonExistingCard() {
        Integer nonExistingId = 102;

        assertThatThrownBy(() -> cardService.fetchCardDetails(nonExistingId))
                .isInstanceOf(CardDoesNotExistException.class)
                .hasMessage("Card with id=" + nonExistingId + " does not exist");
    }
}