package io.hrushik09.tasker.cards;

public class MemberCreatorActionDTOBuilder {
    private String type = "member";
    private Integer id = 1;
    private String text = "Not important";

    private MemberCreatorActionDTOBuilder() {
    }

    private MemberCreatorActionDTOBuilder(MemberCreatorActionDTOBuilder copy) {
        this.type = copy.type;
        this.id = copy.id;
        this.text = copy.text;
    }

    public static MemberCreatorActionDTOBuilder aMemberCreatorActionDTO() {
        return new MemberCreatorActionDTOBuilder();
    }

    public MemberCreatorActionDTOBuilder but() {
        return new MemberCreatorActionDTOBuilder(this);
    }

    public MemberCreatorActionDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public MemberCreatorActionDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public MemberCreatorActionDTOBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public MemberCreatorActionDTO build() {
        return new MemberCreatorActionDTO(type, id, text);
    }
}
