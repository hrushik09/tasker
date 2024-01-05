package io.hrushik09.tasker.lists;

public record UpdateListResponse(
        Integer id,
        String title
) {
    public static UpdateListResponse from(List list) {
        return new UpdateListResponse(list.getId(), list.getTitle());
    }
}
