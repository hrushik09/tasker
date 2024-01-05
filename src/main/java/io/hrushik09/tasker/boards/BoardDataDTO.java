package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.cards.CardMinDTO;
import io.hrushik09.tasker.lists.ListDetailsDTO;

import java.util.List;

public record BoardDataDTO(
        Integer id,
        List<ListDetailsDTO> lists,
        List<CardMinDTO> cards
) {
}
