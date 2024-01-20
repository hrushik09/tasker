package io.hrushik09.tasker.cards;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActionService {
    private final ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<ActionResponse> fetchAllCardActions(Integer cardId) {
        List<Action> actions = actionRepository.findByCardId(cardId);
        return actions.stream().map(ActionResponse::from).toList();
    }

    public void saveCreateCardAction(Card card) {
        Action action = new Action();
        action.setCard(card);
        Integer creatorId = card.getList().getBoard().getUser().getId();
        action.setMemberCreatorId(creatorId);
        action.setType("createCard");
        action.setHappenedAt(Instant.now());
        action.setTranslationKey("action_create_card");
        CardAction cardAction = new CardAction();
        cardAction.setType("card");
        cardAction.setCardId(card.getId());
        cardAction.setText(card.getTitle());
        action.setCardAction(cardAction);
        ListAction listAction = new ListAction();
        listAction.setType("list");
        listAction.setListId(card.getList().getId());
        listAction.setText(card.getList().getTitle());
        action.setListAction(listAction);
        MemberCreatorAction memberCreatorAction = new MemberCreatorAction();
        memberCreatorAction.setType("member");
        memberCreatorAction.setCreatorId(creatorId);
        memberCreatorAction.setText(card.getList().getBoard().getUser().getName());
        action.setMemberCreatorAction(memberCreatorAction);
        actionRepository.save(action);
    }

    public void saveAddDueAction(Card card) {

    }
}
