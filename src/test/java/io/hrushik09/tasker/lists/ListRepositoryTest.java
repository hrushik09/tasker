package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.RepositoryTest;
import io.hrushik09.tasker.users.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ListRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ListRepository listRepository;

    private User havingPersistedUser(String name) {
        User user = new User();
        user.setName(name);
        return entityManager.persist(user);
    }

    private List havingPersistedList(String title, User user) {
        List list = new List();
        list.setTitle(title);
        list.setUser(user);
        return entityManager.persist(list);
    }

    @Test
    void validateFetchForAll() {
        User user = havingPersistedUser("user 1");
        List todo = havingPersistedList("To Do", user);
        List completed = havingPersistedList("Completed", user);
        List deployed = havingPersistedList("Deployed", user);
        List futureWorks = havingPersistedList("Future Works", user);
        User extraUser = havingPersistedUser("user 2");
        List extraList = havingPersistedList("Not important", extraUser);
        entityManager.flush();
        entityManager.clear();

        java.util.List<ListDTO> fetched = listRepository.fetchAllFor(user.getId());

        assertThat(fetched).hasSize(4);
        assertThat(fetched).extracting("id")
                .containsExactlyInAnyOrder(todo.getId(), completed.getId(), deployed.getId(), futureWorks.getId());
        assertThat(fetched).extracting("title")
                .containsExactlyInAnyOrder("To Do", "Completed", "Deployed", "Future Works");
    }
}