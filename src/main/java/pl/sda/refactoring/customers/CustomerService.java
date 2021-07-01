package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.UUID;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public final class CustomerService {

    private final CustomerDao customerDao;
    private final MailSender mailSender;

    CustomerService(CustomerDao customerDao, MailSender mailSender) {
        this.customerDao = requireNonNull(customerDao);
        this.mailSender = requireNonNull(mailSender);
    }

    public boolean registerPerson(RegisterPersonForm form) {
        if (isPersonRegisteredInDatabase(form) || !form.isFormFilled()) {
            return false;
        }
        final Customer customer = Customer.initializePersonWith(form);
        if (!customer.isValidPerson()) {
            return false;
        }

        String subj;
        String body;
        if (form.isVerified()) {
            customer.markVerified();
            subj = "Your are now verified customer!";
            body = "<b>Hi " + form.getFirstName() + "</b><br/>" +
                "Thank you for registering in our service. Now you are verified customer!";
        } else {
            customer.setVerified(false);
            subj = "Waiting for verification";
            body = "<b>Hi " + form.getFirstName() + "</b><br/>" +
                "We registered you in our service. Please wait for verification!";
        }
        customerDao.save(customer);
        mailSender.sendEmail(form.getEmail(), subj, body);
        return true;
    }

    private boolean isPersonRegisteredInDatabase(RegisterPersonForm form) {
        return customerDao.emailExists(form.getEmail()) || customerDao.peselExists(form.getPesel());
    }

    public void registerCompany(RegisterCompanyForm form) {
        form.validate();
        validateCompanyExistence(form.getEmail(), form.getVat());
        final var customer = Customer.initializeCompanyWith(form);

        String subj;
        String body;
        if (form.isVerified()) {
            customer.markVerified();
            subj = "Your are now verified customer!";
            body = "<b>Your company: " + form.getName() + " is ready to make na order.</b><br/>" +
                "Thank you for registering in our service. Now you are verified customer!";
        } else {
            customer.setVerified(false);
            subj = "Waiting for verification";
            body = "<b>Hello</b><br/>" +
                "We registered your company: " + form.getName() + " in our service. Please wait for verification!";
        }
        customerDao.save(customer);
        mailSender.sendEmail(customer.getEmail(), subj, body);
    }

    private void validateCompanyExistence(String email, String vat) {
        if (isCompanyRegistered(email, vat)) {
            throw new CompanyAlreadyExistsException(format(
                "company exists, email: %s, vat: %s", email, vat));
        }
    }

    private boolean isCompanyRegistered(String email, String vat) {
        return customerDao.emailExists(email) || customerDao.vatExists(vat);
    }

    /**
     * Set new address for customer
     * @param cid
     * @param str
     * @param zipcode
     * @param city
     * @param country
     * @return
     */
    public boolean updateAddress(UUID cid, String str, String zipcode, String city, String country) {
        var result = false;
        var customer = customerDao.findById(cid);
        if (customer.isPresent()) {
           var object = customer.get();
           object.setAddrStreet(str);
           object.setAddrZipCode(zipcode);
           object.setAddrCity(city);
           object.setAddrCountryCode(country);
           customerDao.save(object);
           result = true;
        }
        return result;
    }

}
