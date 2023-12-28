package io.hrushik09.tasker.lists;

import io.hrushik09.tasker.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListService {
    private final ListRepository listRepository;
    private final UserService userService;

    public ListService(ListRepository listRepository, UserService userService) {
        this.listRepository = listRepository;
        this.userService = userService;
    }

    @Transactional
    public ListDTO create(CreateListCommand cmd) {
        List list = new List();
        list.setTitle(cmd.title());
        list.setUser(userService.findById(cmd.userId()));
        List saved = listRepository.save(list);
        return ListDTO.from(saved);
    }
}
