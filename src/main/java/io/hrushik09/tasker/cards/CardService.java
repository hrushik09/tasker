package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.List;
import io.hrushik09.tasker.lists.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CardService {
    private static final java.util.List<String> CARD_FIELDS_NOT_ALLOWED_FOR_UPDATE = java.util.List.of("id", "createdAt", "updatedAt");
    private final CardRepository cardRepository;
    private final ListService listService;
    private final CardActionService cardActionService;

    public CardService(CardRepository cardRepository, ListService listService, CardActionService cardActionService) {
        this.cardRepository = cardRepository;
        this.listService = listService;
        this.cardActionService = cardActionService;
    }

    @Transactional
    public CreateCardResponse create(CreateCardCommand cmd) {
        Card card = new Card();
        card.setTitle(cmd.title());
        card.setList(listService.findById(cmd.listId()));
        Card saved = cardRepository.save(card);
        cardActionService.saveCreateCardAction(saved);
        return CreateCardResponse.from(saved);
    }

    @Transactional
    public UpdateCardResponse update(UpdateCardCommand cmd) {
        Map<String, Object> fields = cmd.fields();
        Optional<String> optionalField = CARD_FIELDS_NOT_ALLOWED_FOR_UPDATE.stream()
                .filter(fields::containsKey)
                .findFirst();
        if (optionalField.isPresent()) {
            throw new NotAllowedFieldForUpdateCardException(optionalField.get());
        }

        Card fetched = cardRepository.findById(cmd.id()).orElseThrow(() -> new CardDoesNotExistException(cmd.id()));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Card.class, key);
            if (field == null) {
                throw new InvalidFieldForUpdateCardException(key);
            }
            field.setAccessible(true);
            if ("start".equals(key) || "due".equals(key)) {
                Instant start = Instant.parse((String) value);
                ReflectionUtils.setField(field, fetched, start);
            } else if ("list".equals(key)) {
                List newlist = listService.findById((Integer) value);
                if (!newlist.getBoard().getId().equals(fetched.getList().getBoard().getId())) {
                    throw new ListNotInGivenBoardException((Integer) value);
                }
                fetched.setList(newlist);
            } else {
                ReflectionUtils.setField(field, fetched, value);
            }
            field.setAccessible(false);
        });
        Card updated = cardRepository.save(fetched);
        return UpdateCardResponse.from(updated);
    }

    public AllCardMinDetailsDTO fetchAllFor(Integer boardId) {
        return new AllCardMinDetailsDTO(cardRepository.fetchForAll(boardId));
    }

    public CardMaxDetailsDTO fetchCardDetails(Integer id) {
        Card fetched = cardRepository.findById(id).orElseThrow(() -> new CardDoesNotExistException(id));
        return CardMaxDetailsDTO.from(fetched, java.util.List.of());
    }
}
