package io.hrushik09.tasker.lists;

public record ListDTO(
        Integer id,
        String title
) {
    static ListDTO from(List list) {
        return new ListDTO(list.getId(), list.getTitle());
    }
}
