package model;

import jakarta.persistence.AttributeConverter;
import org.mindrot.jbcrypt.BCrypt;
import javax.persistence.Converter;

@Converter // Marks this class as a JPA attribute converter
public class PasswConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            return null;
        }
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    @Override
    public String convertToEntityAttribute(String hashedPassword) {
        return hashedPassword;
    }
}