package io.hrushik09.tasker.cards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query("SELECT new io.hrushik09.tasker.cards.CardMinDTO(c.id, c.title, c.list.id) FROM Card c WHERE c.list.board.id = :boardId")
    List<CardMinDTO> fetchForAll(Integer boardId);
}
