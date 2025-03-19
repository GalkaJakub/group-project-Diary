package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent extends User {

    /*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParent;

     */

    @Column(name="phoneNumber")
    private String phoneNumber;

    @Column(name="city")
    private String city;

    @Column(name="postCode")
    private String postCode;

    @Column(name="street")
    private String street;

    @Column(name="houseNumber")
    private String houseNumber;

    @Column(name="apartmentNumber")
    private String apartmentNumber;

    //@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "parent_children",
            joinColumns = @JoinColumn(name = "idParent"),
            inverseJoinColumns = @JoinColumn(name = "idStudent")
    )
    private List<Student> children;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    // Konstruktor bezargumentowy
    public Parent() {}

    // Konstruktor z argumentami
    public Parent(String firstName, String lastName, String pesel, String login, String password, char userType, String phoneNumber, String city, String postCode, String street, String houseNumber, String apartmentNumber) {
        this.setFirstName(firstName);
        this.setSurename(lastName);
        this.setPeselNumber(pesel);
        this.setLogin(login);
        this.setPassword(password);
        this.setUserType(userType);
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.postCode = postCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    // Gettery i settery
    /*
    public Long getIdParent() {
        return idParent;
    }

    public void setIdParent(Long idParent) {
        this.idParent = idParent;
    }
    */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public List<Student> getChildren() {
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = children;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}