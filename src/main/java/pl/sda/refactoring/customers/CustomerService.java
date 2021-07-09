package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

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

    public RegisteredCompany registerCompany(RegisterCompanyForm form) {
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
            subj = "Waiting for verification";
            body = "<b>Hello</b><br/>" +
                "We registered your company: " + form.getName() + " in our service. Please wait for verification!";
        }
        customerDao.save(customer);
        mailSender.sendEmail(customer.getEmail(), subj, body);
        return new RegisteredCompany(customer.getId());
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

    public boolean updateAddress(UpdateCustomerAddresss request) {
        var result = false;
        var customer = customerDao.findById(request.getCustomerId());
        if (customer.isPresent()) {
           var object = customer.get();
           object.updateAddress(new Address(request.getStr(),
               request.getCity(),
               request.getZipcode(),
               request.getCountry()));
           customerDao.save(object);
           result = true;
        }
        return result;
    }
}
