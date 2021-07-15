package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import pl.sda.refactoring.customers.CustomerNotifier.RegistrationNotification;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public final class CustomerService {

    private final CustomerDao customerDao;
    private final CustomerNotifier customerNotifier;

    CustomerService(CustomerDao customerDao, CustomerNotifier customerNotifier) {
        this.customerDao = requireNonNull(customerDao);
        this.customerNotifier = requireNonNull(customerNotifier);
    }

    public boolean registerPerson(RegisterPersonForm form) {
        if (isPersonRegisteredInDatabase(form) || !form.isFormFilled()) {
            return false;
        }
        final Customer customer = Customer.initializePersonWith(form);
        if (!customer.isValidPerson()) {
            return false;
        }
        if (form.isVerified()) {
            customer.markVerified();
        }
        customerDao.save(customer);
        customerNotifier.notifyRegistration(new RegistrationNotification(customer.getId(),
            customer.getFullName(),
            customer.getEmail(),
            customer.isVerf()));
        return true;
    }

    private boolean isPersonRegisteredInDatabase(RegisterPersonForm form) {
        return customerDao.emailExists(form.getEmail()) || customerDao.peselExists(form.getPesel());
    }

    public RegisteredCompany registerCompany(RegisterCompanyForm form) {
        form.validate();
        validateCompanyExistence(form.getEmail(), form.getVat());
        final var customer = Customer.initializeCompanyWith(form);
        if (form.isVerified()) {
            customer.markVerified();
        }
        customerDao.save(customer);
        customerNotifier.notifyRegistration(new RegistrationNotification(customer.getId(),
            customer.getCompName(),
            customer.getEmail(),
            customer.isVerf()));
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
