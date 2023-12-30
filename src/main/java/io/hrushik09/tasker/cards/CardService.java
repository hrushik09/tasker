package io.hrushik09.tasker.cards;

import io.hrushik09.tasker.lists.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CardDTO create(CreateCardCommand cmd) {
        Card card = new Card();
        card.setTitle(cmd.title());
        card.setList(listService.getReferenceById(cmd.listId()));
        Card saved = cardRepository.save(card);
        return CardDTO.from(saved);
    }
}