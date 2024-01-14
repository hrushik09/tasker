package io.hrushik09.tasker.cards;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CardActionService {
    public void saveCreateCardAction(Card card) {

    }

    public List<ActionDTO> fetchAllCardActions(Integer id) {
        return null;
    }
}
