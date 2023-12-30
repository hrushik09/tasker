package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListBuilder;
import io.hrushik09.tasker.lists.ListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.lists.ListBuilder.aList;
import static org.assertj.core.api.Assertions.assertThat;
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
        Card card = aCard().withId(cardId)
                .withTitle(title)
                .with(listBuilder)
                .build();
        when(cardRepository.save(any())).thenReturn(card);

        CardDTO created = cardService.create(new CreateCardCommand(listId, title));

        assertThat(created.id()).isEqualTo(cardId);
        assertThat(created.title()).isEqualTo(title);
        assertThat(created.listId()).isEqualTo(listId);
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());
        Card captorValue = cardArgumentCaptor.getValue();
        assertThat(captorValue.getTitle()).isEqualTo(title);
        assertThat(captorValue.getList().getId()).isEqualTo(listId);
    }
}