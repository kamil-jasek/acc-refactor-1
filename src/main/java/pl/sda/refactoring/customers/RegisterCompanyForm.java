package pl.sda.refactoring.customers;

import static java.lang.String.format;

import java.util.regex.Pattern;
import pl.sda.refactoring.customers.exception.InvalidCompanyDataException;
import pl.sda.refactoring.customers.exception.RegisterFormNotFilledException;

public class RegisterCompanyForm {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final String email;
    private final String name;
    private final String vat;
    private final boolean verified;

    /**
     * @param email
     * @param name
     * @param vat
     * @param verified
     */
    public RegisterCompanyForm(String email, String name, String vat, boolean verified) {
        this.email = email;
        this.name = name;
        this.vat = vat;
        this.verified = verified;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getVat() {
        return vat;
    }

    public boolean isVerified() {
        return verified;
    }

    boolean isFilled() {
        return getEmail() != null && getName() != null && getVat() != null;
    }

    void validate() {
        if (!isFilled()) {
            throw new RegisterFormNotFilledException(format("form is incorrectly filled: %s", this));
        }
        if (!hasValidEmail()) {
            throw new InvalidCompanyDataException(format("invalid email: %s", getEmail()));
        }
        if (!hasValidName()) {
            throw new InvalidCompanyDataException(format("invalid name: %s", getName()));
        }
        if (!hasValidVat()) {
            throw new InvalidCompanyDataException(format("invalid vat: %s", getVat()));
        }
    }

    private boolean hasValidEmail() {
        return EMAIL_PATTERN.matcher(getEmail()).matches();
    }

    private boolean hasValidName() {
        return name.matches("[\\p{L}\\s\\.]{2,100}");
    }

    private boolean hasValidVat() {
        return vat.matches("\\d{10}");
    }

    @Override
    public String toString() {
        return "RegisterCompanyForm{" +
            "email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", vat='" + vat + '\'' +
            ", verified=" + verified +
            '}';
    }
}
