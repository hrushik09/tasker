package io.hrushik09.tasker.cards;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    private Card card;
    @Column(nullable = false)
    private Integer memberCreatorId;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Instant happenedAt;
    @Column(nullable = false)
    private String translationKey;
    @OneToOne(cascade = {CascadeType.PERSIST}, optional = false)
    private CardAction cardAction;
    @OneToOne(cascade = {CascadeType.PERSIST}, optional = false)
    private ListAction listAction;
    @OneToOne(cascade = {CascadeType.PERSIST}, optional = false)
    private MemberCreatorAction memberCreatorAction;
    @Column(nullable = false, insertable = false, updatable = false)
    private Instant createdAt;
    @Column(nullable = false, insertable = false, updatable = false)
    private Instant updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
