package io.hrushik09.tasker.lists;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ListRepository extends JpaRepository<List, Integer> {
    @Query("SELECT new io.hrushik09.tasker.lists.ListDTO(l.id, l.title) FROM List l WHERE l.user.id = :userId")
    java.util.List<ListDTO> fetchAllFor(int userId);
}
