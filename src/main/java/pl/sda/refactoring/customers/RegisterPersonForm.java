package pl.sda.refactoring.customers;

import java.util.regex.Pattern;

public final class RegisterPersonForm {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String pesel;
    private final boolean verified;

    public RegisterPersonForm(String email, String firstName, String lastName, String pesel, boolean verified) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public boolean isVerified() {
        return verified;
    }

    boolean isFormFilled() {
        return getEmail() != null && getFirstName() != null && getLastName() != null && getPesel() != null;
    }

    boolean hasValidEmail() {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    boolean hasValidFirstName() {
        return firstName.length() > 0 && firstName.matches("[\\p{L}\\s\\.]{2,100}");
    }

    boolean isValidLastName() {
        return lastName.length() > 0 && lastName.matches("[\\p{L}\\s\\.]{2,100}");
    }

    boolean isValidPesel() {
        return pesel.length() == 11 && pesel.matches("/\\d{11}/");
    }
}
