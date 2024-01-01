package io.hrushik09.tasker.boards;

import io.hrushik09.tasker.lists.ListDTO;

import java.util.List;

public record BoardDataDTO(
        Integer id,
        List<ListDTO> lists,
        List<CardMinDTO> cards
) {
}
