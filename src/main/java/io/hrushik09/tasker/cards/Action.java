package io.hrushik09.tasker.cards;

import java.time.Instant;

public class Action {
    private Integer memberCreatorId;
    private String type;
    private Instant happenedAt;
    private String translationKey;
    private CardAction cardAction;
    private ListAction listAction;
    private MemberCreatorAction memberCreatorAction;

    public Integer getMemberCreatorId() {
        return memberCreatorId;
    }

    public void setMemberCreatorId(Integer memberCreatorId) {
        this.memberCreatorId = memberCreatorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getHappenedAt() {
        return happenedAt;
    }

    public void setHappenedAt(Instant happenedAt) {
        this.happenedAt = happenedAt;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public void setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
    }

    public CardAction getCardAction() {
        return cardAction;
    }

    public void setCardAction(CardAction cardAction) {
        this.cardAction = cardAction;
    }

    public ListAction getListAction() {
        return listAction;
    }

    public void setListAction(ListAction listAction) {
        this.listAction = listAction;
    }

    public MemberCreatorAction getMemberCreatorAction() {
        return memberCreatorAction;
    }

    public void setMemberCreatorAction(MemberCreatorAction memberCreatorAction) {
        this.memberCreatorAction = memberCreatorAction;
    }
}
