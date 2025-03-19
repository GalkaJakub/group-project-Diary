package model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.example.HibernateUtil;
import org.hibernate.Session;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMessage;

    @ManyToOne
    @JoinColumn(name="idReceiver")
    private User receiver;

    @ManyToOne
    @JoinColumn(name="idSender")
    private User sender;

    @Column(name="title")
    private String title;

    @Column(name="messageSource")
    private String messageSource;

    @Column(name="date")
    private LocalDateTime sentDate;
    
    @Column (name="status")
    private Boolean status = false;

    // Konstruktor bezargumentowy
    public Message() {}

    // Konstruktor z argumentami
    public Message(User sender, User receiver, String title, String messageSource) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.messageSource = messageSource;
    }

    // Gettery i settery
    public Long getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(Long idMessage) {
        this.idMessage = idMessage;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(String messageSource) {
        this.messageSource = messageSource;
    }

    public void setSentDate(LocalDateTime sentDate) {this.sentDate = sentDate;}

    public LocalDateTime getSentDate(){return sentDate;}

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public Boolean changeStatus()
    {
        Boolean flag = false;
        status = true;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            org.hibernate.Transaction tx = session.beginTransaction();
            
           String hql = "UPDATE Message SET status = :status WHERE id = :id";
            int updatedRows = session.createQuery(hql)
                         .setParameter("status", true)
                         .setParameter("id", this.idMessage)
                         .executeUpdate();
            
            if (updatedRows > 0) {
            tx.commit(); 
            flag = true;
            } else {
                tx.rollback(); // Wycofanie transakcji, jeśli nic nie zmieniono
            }
            
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
            return flag;
        } finally {
            session.close(); // Zamykamy sesję
        }
        
        return flag;
    }
}