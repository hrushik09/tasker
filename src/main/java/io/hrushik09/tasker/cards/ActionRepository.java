package io.hrushik09.tasker.cards;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    List<ActionResponse> findActionDetailsByCardId(Integer cardId);
}
