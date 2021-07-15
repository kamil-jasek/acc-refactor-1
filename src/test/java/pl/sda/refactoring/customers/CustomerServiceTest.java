package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;
import pl.sda.refactoring.customers.exception.InvalidCompanyDataException;
import pl.sda.refactoring.customers.exception.RegisterFormNotFilledException;

public class CustomerServiceTest {

    private final TestCustomerDao dao = new TestCustomerDao();
    private final CustomerService customerService = new CustomerService(dao, notification -> {});

    @ParameterizedTest
    @CsvSource({
        "test@test.com,Test S.A.,9302030403",
        "test2@test2.com,Testyy S.A.,9304440403"
    })
    public void shouldRegisterVerifiedCompany(String email, String name, String vat) {
        // when
        final var registeredCompany =  customerService
            .registerCompany(new RegisterCompanyForm(email, name, vat, true));

        // then
        final var customer = dao.getCompanyById(registeredCompany.getId());
        assertEquals(registeredCompany.getId(), customer.getId());
        assertEquals(customer.getEmail(), email);
        assertEquals(customer.getCompName(), name);
        assertEquals(customer.getCompVat(), vat);
        assertTrue(customer.isVerf());
        assertEquals(customer.getVerifBy(), CustomerVerifier.AUTO_EMAIL);
        assertNotNull(customer.getVerfTime());
    }

    @Test
    public void shouldNotRegisterCompanyIfAlreadyExists() {
        // given
        final var customer = Customer.initializeCompanyWith(new RegisterCompanyForm(
            "mail@comp.com",
            "Test S.A.",
            "9439499393",
            true
        ));
        dao.save(customer);

        // when
        assertThrows(CompanyAlreadyExistsException.class, () -> customerService.registerCompany(
            new RegisterCompanyForm("mail@comp.com", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void shouldNotRegisterCompanyIfFormIsNotFilled() {
        assertThrows(RegisterFormNotFilledException.class, () -> customerService.registerCompany(
            new RegisterCompanyForm(null, null, null, false)));
    }

    @Test
    public void shouldNotRegisterCompanyIfEmailInvalid() {
        assertThrows(InvalidCompanyDataException.class, () -> customerService.registerCompany(
            new RegisterCompanyForm("invalid@", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void shouldNotRegisterCompanyIfNameInvalid() {
        assertThrows(InvalidCompanyDataException.class, () -> customerService.registerCompany(
            new RegisterCompanyForm("test@ok.com", "F&", "9302030403", true)));
    }

    @Test
    public void shouldNotRegisterCompanyIfDataInvalid() {
        assertThrows(InvalidCompanyDataException.class, () -> customerService.registerCompany(
            new RegisterCompanyForm("test@ok.com", "TestOK", "AB02030403", true)));
    }
}
