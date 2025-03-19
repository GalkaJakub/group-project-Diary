package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "fundraisings")
public class Fundraising {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFundrasing;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmployee")
    private Employee employee;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="targetAmount")
    private BigDecimal targetAmount;

    @OneToMany(mappedBy = "fundraising", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionsList;

    // Konstruktor bezargumentowy
    public Fundraising() {}

    // Konstruktor z argumentami
    public Fundraising(Employee employee, String name, String description, BigDecimal targetAmount, List<Transaction> transactionsList) {
        this.employee = employee;
        this.name = name;
        this.description = description;
        this.targetAmount = targetAmount;
        this.transactionsList = transactionsList;
    }

    // Gettery i settery

    public Long getIdFundrasing() {
        return idFundrasing;
    }

    public void setIdFundrasing(Long idFundrasing) {
        this.idFundrasing = idFundrasing;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public List<Transaction> getTransactionsList() {
        return transactionsList;
    }

    public void setTransactionsList(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }
}
