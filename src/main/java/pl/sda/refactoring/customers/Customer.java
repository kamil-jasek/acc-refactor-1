package pl.sda.refactoring.customers;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The customer, can be person or company
 */
public class Customer {

    // customer types
    public static final int COMPANY = 1;
    public static final int PERSON = 2;

    private UUID id;
    private int type;
    private LocalDateTime ctime;
    private String email;
    private LocalDateTime verfTime;
    private boolean verf;
    private CustomerVerifier verifBy;

    // company data
    private String compName;
    private String compVat;

    // person data
    private String fName;
    private String lName;
    private String pesel;

    private Address address;

    @FrameworkOnly
    private Customer() {
    }

    Customer(String email, String companyName, String vat) {
        this.id = UUID.randomUUID();
        this.type = COMPANY;
        this.ctime = LocalDateTime.now();
        this.email = requireNonNull(email);
        this.compName = requireNonNull(companyName);
        this.compVat = requireNonNull(vat);
    }

    static Customer initializePersonWith(RegisterPersonForm form) {
        final var customer = new Customer();
        customer.initializePerson(form);
        return customer;
    }

    static Customer initializeCompanyWith(RegisterCompanyForm form) {
        return new Customer(form.getEmail(), form.getName(), form.getVat());
    }

    void initializePerson(RegisterPersonForm form) {
        this.type = PERSON;
        this.id = UUID.randomUUID();
        this.ctime = LocalDateTime.now();
        if (form.hasValidEmail()) {
            this.email = form.getEmail();
        }
        if (form.hasValidFirstName()) {
            this.fName = form.getFirstName();
        }
        if (form.isValidLastName()) {
            this.lName = form.getLastName();
        }
        if (form.isValidPesel()) {
            this.pesel = form.getPesel();
        }
    }

    public UUID getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public String getCompName() {
        return compName;
    }

    public String getCompVat() {
        return compVat;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getPesel() {
        return pesel;
    }

    public void updateAddress(Address address) {
        this.address = requireNonNull(address);
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getVerfTime() {
        return verfTime;
    }

    public boolean isVerf() {
        return verf;
    }

    public CustomerVerifier getVerifBy() {
        return verifBy;
    }

    boolean isValidPerson() {
        return getEmail() != null && getfName() != null && getlName() != null && getPesel() != null;
    }

    public String getFullName() {
        return fName + " " + lName;
    }

    void markVerified() {
        this.verf = true;
        this.verfTime = LocalDateTime.now();
        this.verifBy = CustomerVerifier.AUTO_EMAIL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return type == customer.type && verf == customer.verf && Objects.equals(id, customer.id)
            && Objects.equals(ctime, customer.ctime) && Objects.equals(email, customer.email)
            && Objects.equals(verfTime, customer.verfTime) && verifBy == customer.verifBy && Objects
            .equals(compName, customer.compName) && Objects.equals(compVat, customer.compVat) && Objects
            .equals(fName, customer.fName) && Objects.equals(lName, customer.lName) && Objects
            .equals(pesel, customer.pesel) && Objects.equals(address.getAddrStreet(), customer.address.getAddrStreet()) && Objects
            .equals(address.getAddrCity(), customer.address.getAddrCity()) && Objects.equals(address.getAddrZipCode(),
            customer.address.getAddrZipCode())
            && Objects.equals(address.getAddrCountryCode(), customer.address.getAddrCountryCode());
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, type, ctime, email, verfTime, verf, verifBy, compName, compVat, fName, lName, pesel,
                address.getAddrStreet(),
                address.getAddrCity(), address.getAddrZipCode(), address.getAddrCountryCode());
    }

    boolean isValidCompany() {
        return getEmail() != null && getCompName() != null && getCompVat() != null;
    }
}
