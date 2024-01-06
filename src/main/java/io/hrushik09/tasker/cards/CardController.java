package io.hrushik09.tasker.cards;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateCardResponse create(@RequestParam Integer listId, @RequestBody @Valid CreateCardRequest request) {
        return cardService.create(new CreateCardCommand(listId, request.title()));
    }

    @PutMapping("/{id}")
    UpdateCardDescriptionResponse updateDescription(@PathVariable Integer id, @RequestBody @Valid UpdateDescriptionRequest request) {
        return cardService.updateDescription(new UpdateDescriptionCommand(id, request.description()));
    }

    @GetMapping("/{id}")
    CardMaxDetailsDTO fetchCardDetails(@PathVariable Integer id) {
        return cardService.fetchCardDetails(id);
    }
}
