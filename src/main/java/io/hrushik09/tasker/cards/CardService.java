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
    private final ActionService actionService;

    public CardService(CardRepository cardRepository, ListService listService, ActionService actionService) {
        this.cardRepository = cardRepository;
        this.listService = listService;
        this.actionService = actionService;
    }

    @Transactional
    public CreateCardResponse create(CreateCardCommand cmd) {
        Card card = new Card();
        card.setTitle(cmd.title());
        card.setList(listService.findById(cmd.listId()));
        Card saved = cardRepository.save(card);
        actionService.saveCreateCardAction(saved);
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
            switch (key) {
                case "start" -> {
                    Instant start = Instant.parse((String) value);
                    ReflectionUtils.setField(field, fetched, start);
                }
                case "due" -> {
                    Instant due = Instant.parse((String) value);
                    ReflectionUtils.setField(field, fetched, due);
                }
                case "list" -> {
                    List newlist = listService.findById((Integer) value);
                    if (!newlist.getBoard().getId().equals(fetched.getList().getBoard().getId())) {
                        throw new ListNotInGivenBoardException((Integer) value);
                    }
                    fetched.setList(newlist);
                }
                default -> ReflectionUtils.setField(field, fetched, value);
            }
            field.setAccessible(false);
        });
        Card updated = cardRepository.save(fetched);
        fields.forEach((key, value) -> {
            if ("due".equals(key)) {
                actionService.saveAddDueAction(updated);
            }
        });
        return UpdateCardResponse.from(updated);
    }

    public AllCardMinDetailsDTO fetchAllFor(Integer boardId) {
        return new AllCardMinDetailsDTO(cardRepository.fetchForAll(boardId));
    }

    public CardMaxDetailsDTO fetchCardDetails(Integer id) {
        Card fetched = cardRepository.findById(id).orElseThrow(() -> new CardDoesNotExistException(id));
        java.util.List<ActionResponse> actionDTOS = actionService.fetchAllCardActions(id);
        return CardMaxDetailsDTO.from(fetched, actionDTOS);
    }
}
