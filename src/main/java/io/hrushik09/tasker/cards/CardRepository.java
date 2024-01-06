package io.hrushik09.tasker.cards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {
    @Query("SELECT new io.hrushik09.tasker.cards.CardMinDetailsDTO(c.id, c.title, c.list.id) FROM Card c WHERE c.list.board.id = :boardId")
    List<CardMinDetailsDTO> fetchForAll(Integer boardId);

    @Query("SELECT new io.hrushik09.tasker.cards.CardMaxDetailsDTO(c.id, c.title, c.description, c.list.id, c.createdAt, c.updatedAt) FROM Card c WHERE c.id = :id")
    Optional<CardMaxDetailsDTO> findCardDetailsById(Integer id);
}
