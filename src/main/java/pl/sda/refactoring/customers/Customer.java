package pl.sda.refactoring.customers;

import static java.lang.String.format;

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

    // address data
    private String addrStreet;
    private String addrCity;
    private String addrZipCode;
    private String addrCountryCode;

    static Customer initializePersonWith(RegisterPersonForm form) {
        final var customer = new Customer();
        customer.initializePerson(form);
        return customer;
    }

    static Customer initializeCompanyWith(RegisterCompanyForm form) {
        final var customer = new Customer();
        customer.initializeCompany(form);
        return customer;
    }

    private void initializeCompany(RegisterCompanyForm form) {
        this.id = UUID.randomUUID();
        this.type = COMPANY;
        this.ctime = LocalDateTime.now();
        this.email = form.getEmail();
        this.compName = form.getName();
        this.compVat = form.getVat();
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

    public void setId(UUID id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompVat() {
        return compVat;
    }

    public void setCompVat(String compVat) {
        this.compVat = compVat;
    }

    public String getfName() {
        return fName;
    }

    public void setFirstName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setLastName(String lName) {
        this.lName = lName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getAddrStreet() {
        return addrStreet;
    }

    public void setAddrStreet(String addrStreet) {
        this.addrStreet = addrStreet;
    }

    public String getAddrCity() {
        return addrCity;
    }

    public void setAddrCity(String addrCity) {
        this.addrCity = addrCity;
    }

    public String getAddrZipCode() {
        return addrZipCode;
    }

    public void setAddrZipCode(String addrZipCode) {
        this.addrZipCode = addrZipCode;
    }

    public String getAddrCountryCode() {
        return addrCountryCode;
    }

    public void setAddrCountryCode(String addrCountryCode) {
        this.addrCountryCode = addrCountryCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getVerfTime() {
        return verfTime;
    }

    public void setVerificationTime(LocalDateTime verfTime) {
        this.verfTime = verfTime;
    }

    public boolean isVerf() {
        return verf;
    }

    public void setVerified(boolean verf) {
        this.verf = verf;
    }

    public CustomerVerifier getVerifBy() {
        return verifBy;
    }

    public void setVerifiedBy(CustomerVerifier verifBy) {
        this.verifBy = verifBy;
    }

    boolean isValidPerson() {
        return getEmail() != null && getfName() != null && getlName() != null && getPesel() != null;
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
            .equals(pesel, customer.pesel) && Objects.equals(addrStreet, customer.addrStreet) && Objects
            .equals(addrCity, customer.addrCity) && Objects.equals(addrZipCode, customer.addrZipCode)
            && Objects.equals(addrCountryCode, customer.addrCountryCode);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, type, ctime, email, verfTime, verf, verifBy, compName, compVat, fName, lName, pesel, addrStreet,
                addrCity, addrZipCode, addrCountryCode);
    }

    boolean isValidCompany() {
        return getEmail() != null && getCompName() != null && getCompVat() != null;
    }
}
