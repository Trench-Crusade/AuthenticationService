package org.tc.authservice.core.domain.validators;

import org.tc.authservice.core.domain.User;
import org.tc.authservice.shared.exceptions.general.detailed.TCUtilityClassException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserValidator {

    private UserValidator() throws TCUtilityClassException {
        throw new TCUtilityClassException("Utility class");
    }

    public static List<String> validateCreatedUser(User user){
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add(validateUsername(user));
        errorMessages.add(validateEmail(user));
        return errorMessages.stream().filter(Objects::nonNull).toList();
    }

    private static String validateUsername(User user){
        String username = user.getUsername();
        if(username.isBlank()
                || username.isEmpty()
        ) {
            return "Username is empty";
        }
        if(username.length()<3){
            return "Username is too short";
        }
        if(username.length()>20){
            return "Username is too long";
        }
        return null;
    }

    private static String validateEmail(User user){
        String email = user.getEmail();
        if(email.isBlank()
                || email.isEmpty()
        ) {
            return "Email is empty";
        }
        return null;
    }
}
