package io.hrushik09.tasker.cards;

import java.time.Instant;

import static io.hrushik09.tasker.cards.ActionDisplayDTOBuilder.anActionDisplayDTOBuilder;

public class ActionResponseBuilder {
    private Integer id = 1;
    private Integer memberCreatorId = 1;
    private String type = "Not important";
    private Instant happenedAt = Instant.parse("2023-12-12T12:12:12Z");
    private ActionDisplayDTOBuilder actionDisplayDTOBuilder = anActionDisplayDTOBuilder();

    private ActionResponseBuilder() {
    }

    private ActionResponseBuilder(ActionResponseBuilder copy) {
        this.id = copy.id;
        this.memberCreatorId = copy.memberCreatorId;
        this.type = copy.type;
        this.happenedAt = copy.happenedAt;
        this.actionDisplayDTOBuilder = copy.actionDisplayDTOBuilder;
    }

    public static ActionResponseBuilder anActionResponseBuilder() {
        return new ActionResponseBuilder();
    }

    public ActionResponseBuilder but() {
        return new ActionResponseBuilder(this);
    }

    public ActionResponseBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ActionResponseBuilder withMemberCreatorId(Integer memberCreatorId) {
        this.memberCreatorId = memberCreatorId;
        return this;
    }

    public ActionResponseBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ActionResponseBuilder withHappenedAt(Instant happenedAt) {
        this.happenedAt = happenedAt;
        return this;
    }

    public ActionResponseBuilder with(ActionDisplayDTOBuilder actionDisplayDTOBuilder) {
        this.actionDisplayDTOBuilder = actionDisplayDTOBuilder;
        return this;
    }

    public ActionResponse build() {
        return new ActionResponse(id, memberCreatorId, type, happenedAt, actionDisplayDTOBuilder.build());
    }
}
