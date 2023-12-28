package io.hrushik09.tasker.lists;

public record ListDTO(
        int id,
        String title,
        int userId
) {
    static ListDTO from(List list) {
        return new ListDTO(list.getId(), list.getTitle(), list.getUserId());
    }
}
