package io.hrushik09.tasker.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListRepository extends JpaRepository<List, Integer> {
    @Query("SELECT new io.hrushik09.tasker.lists.ListDetailsDTO(l.id, l.title) FROM List l WHERE l.board.id = :boardId")
    java.util.List<ListDetailsDTO> fetchAllFor(Integer boardId);
}
