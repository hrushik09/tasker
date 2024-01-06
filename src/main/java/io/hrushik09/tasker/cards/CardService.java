package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Service
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final ListService listService;

    public CardService(CardRepository cardRepository, ListService listService) {
        this.cardRepository = cardRepository;
        this.listService = listService;
    }

    @Transactional
    public CreateCardResponse create(CreateCardCommand cmd) {
        Card card = new Card();
        card.setTitle(cmd.title());
        card.setList(listService.getReferenceById(cmd.listId()));
        Card saved = cardRepository.save(card);
        return CreateCardResponse.from(saved);
    }

    @Transactional
    public UpdateCardResponse update(UpdateCardCommand cmd) {
        Card fetched = cardRepository.findById(cmd.id()).orElseThrow(() -> new CardDoesNotExistException(cmd.id()));
        cmd.fields().forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Card.class, key);
            if (field == null) {
                throw new InvalidFieldForUpdateCardException(key);
            }
            field.setAccessible(true);
            ReflectionUtils.setField(field, fetched, value);
            field.setAccessible(false);
        });
        Card updated = cardRepository.save(fetched);
        return UpdateCardResponse.from(updated);
    }

    public AllCardMinDetailsDTO fetchAllFor(Integer boardId) {
        return new AllCardMinDetailsDTO(cardRepository.fetchForAll(boardId));
    }

    public CardMaxDetailsDTO fetchCardDetails(Integer id) {
        return cardRepository.findCardDetailsById(id).orElseThrow(() -> new CardDoesNotExistException(id));
    }
}
