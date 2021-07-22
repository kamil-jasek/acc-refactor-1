package pl.sda.refactoring.customers;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.UUID;
import pl.sda.refactoring.customers.CustomerNotifier.RegistrationNotification;
import pl.sda.refactoring.customers.event.EventCreator;
import pl.sda.refactoring.customers.event.EventCreator.Type;
import pl.sda.refactoring.customers.event.EventPublisher;
import pl.sda.refactoring.customers.event.RegisterCustomerEvent;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;

public final class CustomerService {

    private final CustomerDao customerDao;
    private final EventPublisher eventPublisher;

    CustomerService(CustomerDao customerDao, EventPublisher eventPublisher) {
        this.customerDao = requireNonNull(customerDao);
        this.eventPublisher = requireNonNull(eventPublisher);
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
        eventPublisher.publish(new RegisterCustomerEvent(LocalDateTime.now(),
            UUID.randomUUID().toString(),
            new EventCreator("system", Type.SYSTEM),
            customer.getId(),
            customer.getFullName(),
            "PERSON"));
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
