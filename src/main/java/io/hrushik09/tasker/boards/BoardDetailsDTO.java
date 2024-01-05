package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.CardMinDetailsDTO;
import io.hrushik09.tasker.lists.ListDetailsDTO;

import java.util.List;

public record BoardDetailsDTO(
        Integer id,
        List<ListDetailsDTO> lists,
        List<CardMinDetailsDTO> cards
) {
}
