package model;

import org.example.HibernateUtil;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    private static MyModel model = MyModel.getModel();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(name="firstName")
    private String firstName;

    @Column(name="surename")
    private String surename;

    @Column(name="peselNumber")
    private String peselNumber;

    @Column(name="login")
    private String login;

    @Column(name="password")
    //@Convert(converter = PasswConverter.class)
    private String password;

    @Column(name="type")
    private char userType;

    // Relacje - wiadomości wysłane i odebrane
    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<Message> receivedMessages;

    // Konstruktor bezargumentowy
    public User() {}

    // Konstruktor z argumentami
    public User(String firstName, String surename, String peselNumber, String login, String password, char userType) {
        this.firstName = firstName;
        this.surename = surename;
        this.peselNumber = peselNumber;
        this.login = login;
        this.password = password;
        this.userType = userType;
    }

    // Gettery i settery
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurename() {
        return surename;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public String getPeselNumber() {
        return peselNumber;
    }

    public void setPeselNumber(String peselNumber) {
        this.peselNumber = peselNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public char getUserType() {
        return userType;
    }


    public void setUserType(char userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser) && Objects.equals(firstName, user.firstName) && Objects.equals(surename, user.surename) && Objects.equals(peselNumber, user.peselNumber) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(sentMessages, user.sentMessages) && Objects.equals(receivedMessages, user.receivedMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, firstName, surename, peselNumber, login, password, sentMessages, receivedMessages);
    }

    public static User authentication(String login, String password)
    {

        User user = null;
        Session session = HibernateUtil.getSessionFactory().openSession(); // Otwieramy sesję

        try {
            // Tworzymy zapytanie HQL
            String hql = "FROM User WHERE login = :login";
            user = session.createQuery(hql, User.class)
                    .setParameter("login", login) // Ustawiamy login w zapytaniu
                    .uniqueResult();

            // Jeżeli użytkownik istnieje, sprawdzamy hasło (np. za pomocą porównania)
            if (user != null) {
                // Sprawdzamy, czy podane hasło jest poprawne
                if (BCrypt.checkpw(password, user.getPassword()))
                {
                    System.out.println("Saying Hi " + user.getFirstName() + " " + user.getSurename());
                    model.loggedTypeUser(user);

                } else {
                    System.out.println("Incorrect password.");
                    user = null; // Ustawiamy user na null, jeśli hasło jest błędne
                }
            } else {
                System.out.println("That user doesn't exist.");
            }
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }

        return user;
    }

    public void sendMessage(User sender, User recipient, String title, String message)
    {
        // Walidacja danych wejściowych
        if (sender == null || recipient == null) {
            throw new IllegalArgumentException("Sender and recipient cannot be null.");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty.");
        }

        // Tworzenie nowej wiadomości
        Message newMessage = new Message(sender, recipient, title, message);
        newMessage.setSentDate(LocalDateTime.now());

        saveMessageToDatabase(newMessage);

        // Log
        System.out.println("Message sent from " + sender.getFirstName() + " " + sender.getSurename() + " to " + recipient.getFirstName() + " " + recipient.getSurename());
    }


    private Boolean saveMessageToDatabase(Message newMessage)
    {
        Boolean result = false;
        // Zapis wiadomości w bazie danych
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            session.beginTransaction();
            session.persist(newMessage);
            session.getTransaction().commit();
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send the message.");
        }
        return result;
    }

    public List<Message> reloadMessages(User receiver)
    {
        List<Message> messages = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try
        {
            String hql = "FROM Message WHERE receiver = :receiver ORDER BY sentDate DESC";

            messages = session.createQuery(hql, Message.class).setParameter("receiver", receiver).list(); // Pobieramy wyniki jako listę

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close(); // Zamykamy sesję
        }


        return messages;
    }


}