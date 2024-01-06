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
    public CreateCardResponse create(CreateCardCommand cmd) {
        Card card = new Card();
        card.setTitle(cmd.title());
        card.setList(listService.getReferenceById(cmd.listId()));
        Card saved = cardRepository.save(card);
        return CreateCardResponse.from(saved);
    }

    public UpdateCardDescriptionResponse updateDescription(UpdateDescriptionCommand cmd) {
        Card fetched = cardRepository.findById(cmd.id()).orElseThrow(() -> new CardDoesNotExistException(cmd.id()));
        fetched.setDescription(cmd.description());
        Card updated = cardRepository.save(fetched);
        return UpdateCardDescriptionResponse.from(updated);
    }

    public AllCardMinDetailsDTO fetchAllFor(Integer boardId) {
        return new AllCardMinDetailsDTO(cardRepository.fetchForAll(boardId));
    }

    public CardMaxDetailsDTO fetchCardDetails(Integer id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardDoesNotExistException(id));
        return CardMaxDetailsDTO.from(card);
    }
}
