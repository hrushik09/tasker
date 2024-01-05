package io.hrushik09.tasker.lists;

public record CreateListResponse(
        Integer id,
        String title
) {
    static CreateListResponse from(List list) {
        return new CreateListResponse(list.getId(), list.getTitle());
    }
}
