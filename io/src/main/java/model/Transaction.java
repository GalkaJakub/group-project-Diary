package model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idParent")
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idFundraising")
    private Fundraising fundraising;

    @Column(name="idTarget")
    private int idTarget;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="paymentStatus")
    private Boolean paymentStatus;

    // Konstruktor bezargumentowy
    public Transaction() {}

    // Konstruktor z argumentami
    public Transaction(Parent parent, Fundraising fundraising, int idTarget, BigDecimal amount, Boolean paymentStatus) {
        this.parent = parent;
        this.fundraising = fundraising;
        this.idTarget = idTarget;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Gettery i settery

    public Long getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Long idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Fundraising getFundraising() {
        return fundraising;
    }

    public void setFundraising(Fundraising fundraising) {
        this.fundraising = fundraising;
    }

    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
