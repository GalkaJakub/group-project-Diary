package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSalary;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="date")
    private LocalDate dateOfSalary;

    @Column(name="payslip")
    private String payslip;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idEmployee")
    private Employee employee;

    // Konstruktor bezargumentowy
    public Salary() {}

    // Konstruktor z argumentami
    public Salary(BigDecimal amount, LocalDate dateOfSalary, String payslip, Employee employee) {
        this.amount = amount;
        this.dateOfSalary = dateOfSalary;
        this.payslip = payslip;
        this.employee = employee;
    }

    // Gettery i settery
    public Long getIdSalary() {
        return idSalary;
    }

    public void setIdSalary(Long idSalary) {
        this.idSalary = idSalary;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDateOfSalary() {
        return dateOfSalary;
    }

    public void setDateOfSalary(LocalDate dateOfSalary) {
        this.dateOfSalary = dateOfSalary;
    }

    public String getPayslip() {
        return payslip;
    }

    public void setPayslip(String payslip) {
        this.payslip = payslip;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}