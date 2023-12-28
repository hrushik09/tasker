package io.hrushik09.tasker.lists;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ListService {
    private final ListRepository listRepository;

    public ListService(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Transactional
    public ListDTO create(CreateListCommand cmd) {
        List list = new List();
        list.setTitle(cmd.title());
        list.setUserId(cmd.userId());
        List saved = listRepository.save(list);
        return ListDTO.from(saved);
    }
}
