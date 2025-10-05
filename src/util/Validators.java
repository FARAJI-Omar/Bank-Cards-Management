package util;

public class Validators {
    // email validation regex
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
    // phone number validation regex - must be exactly 10 digits
    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{10}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }
    // name validation
    public static boolean isValidName(String name) {
        String nameRegex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
        return name != null && name.matches(nameRegex);
    }
}
