package io.hrushik09.tasker.cards;

import java.time.Instant;

import static io.hrushik09.tasker.cards.CardActionBuilder.aCardActionBuilder;
import static io.hrushik09.tasker.cards.CardBuilder.aCard;
import static io.hrushik09.tasker.cards.DateActionBuilder.aDateActionBuilder;
import static io.hrushik09.tasker.cards.ListActionBuilder.aListActionBuilder;
import static io.hrushik09.tasker.cards.MemberCreatorActionBuilder.aMemberCreatorActionBuilder;

public class ActionBuilder {
    private Integer id = 1;
    private CardBuilder cardBuilder = aCard();
    private Integer memberCreatorId = 1;
    private String type = "Not important";
    private Instant happenedAt = Instant.parse("2023-01-01T01:01:01Z");
    private String translationKey = "Not important";
    private CardActionBuilder cardActionBuilder = aCardActionBuilder();
    private ListActionBuilder listActionBuilder = aListActionBuilder();
    private MemberCreatorActionBuilder memberCreatorActionBuilder = aMemberCreatorActionBuilder();
    private DateActionBuilder dateActionBuilder = aDateActionBuilder();
    private Instant createdAt = Instant.parse("2023-01-01T00:01:01Z");
    private Instant updatedAt = Instant.parse("2023-01-01T00:01:01Z");

    private ActionBuilder() {
    }

    private ActionBuilder(ActionBuilder copy) {
        this.id = copy.id;
        this.cardBuilder = copy.cardBuilder;
        this.memberCreatorId = copy.memberCreatorId;
        this.type = copy.type;
        this.happenedAt = copy.happenedAt;
        this.translationKey = copy.translationKey;
        this.cardActionBuilder = copy.cardActionBuilder;
        this.listActionBuilder = copy.listActionBuilder;
        this.memberCreatorActionBuilder = copy.memberCreatorActionBuilder;
        this.dateActionBuilder = copy.dateActionBuilder;
        this.createdAt = copy.createdAt;
        this.updatedAt = copy.updatedAt;
    }

    public static ActionBuilder anActionBuilder() {
        return new ActionBuilder();
    }

    public ActionBuilder but() {
        return new ActionBuilder(this);
    }

    public ActionBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ActionBuilder with(CardBuilder cardBuilder) {
        this.cardBuilder = cardBuilder;
        return this;
    }

    public ActionBuilder withMemberCreatorId(Integer memberCreatorId) {
        this.memberCreatorId = memberCreatorId;
        return this;
    }

    public ActionBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public ActionBuilder withHappenedAt(Instant happenedAt) {
        this.happenedAt = happenedAt;
        return this;
    }

    public ActionBuilder withTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        return this;
    }

    public ActionBuilder with(CardActionBuilder cardActionBuilder) {
        this.cardActionBuilder = cardActionBuilder;
        return this;
    }

    public ActionBuilder with(ListActionBuilder listActionBuilder) {
        this.listActionBuilder = listActionBuilder;
        return this;
    }

    public ActionBuilder with(MemberCreatorActionBuilder memberCreatorActionBuilder) {
        this.memberCreatorActionBuilder = memberCreatorActionBuilder;
        return this;
    }

    public ActionBuilder with(DateActionBuilder dateActionBuilder) {
        this.dateActionBuilder = dateActionBuilder;
        return this;
    }

    public Action build() {
        Action action = new Action();
        action.setId(id);
        action.setCard(cardBuilder.build());
        action.setMemberCreatorId(memberCreatorId);
        action.setType(type);
        action.setHappenedAt(happenedAt);
        action.setTranslationKey(translationKey);
        action.setCardAction(cardActionBuilder.build());
        action.setListAction(listActionBuilder.build());
        action.setMemberCreatorAction(memberCreatorActionBuilder.build());
        action.setDateAction(dateActionBuilder.build());
        action.setCreatedAt(createdAt);
        action.setUpdatedAt(updatedAt);
        return action;
    }
}
