package io.hrushik09.tasker.cards;

import java.time.Instant;

public class MemberCreatorActionBuilder {
    private Integer id = 1;
    private String type = "member";
    private Integer creatorId = 1;
    private String text = "Not important";
    private Instant createdAt = Instant.parse("2023-06-04T02:02:02Z");
    private Instant updatedAt = Instant.parse("2023-06-04T03:02:02Z");

    private MemberCreatorActionBuilder() {
    }

    private MemberCreatorActionBuilder(MemberCreatorActionBuilder copy) {
        this.id = copy.id;
        this.type = copy.type;
        this.creatorId = copy.creatorId;
        this.text = copy.text;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static MemberCreatorActionBuilder aMemberCreatorActionBuilder() {
        return new MemberCreatorActionBuilder();
    }

    public MemberCreatorActionBuilder but() {
        return new MemberCreatorActionBuilder(this);
    }

    public MemberCreatorActionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public MemberCreatorActionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public MemberCreatorActionBuilder withCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
        return this;
    }

    public MemberCreatorActionBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public MemberCreatorAction build() {
        MemberCreatorAction memberCreatorAction = new MemberCreatorAction();
        memberCreatorAction.setId(id);
        memberCreatorAction.setType(type);
        memberCreatorAction.setCreatorId(creatorId);
        memberCreatorAction.setText(text);
        memberCreatorAction.setCreatedAt(createdAt);
        memberCreatorAction.setUpdatedAt(updatedAt);
        return memberCreatorAction;
    }
}
