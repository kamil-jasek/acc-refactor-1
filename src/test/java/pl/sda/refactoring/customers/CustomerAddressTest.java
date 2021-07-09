package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

final class CustomerAddressTest {

    @ParameterizedTest
    @CsvSource({
        "str,01-500,Wawa,PL",
        "str,,City,US"
    })
    void shouldUpdateCustomerAddress(String street, String zipCode, String city, String country) {
        // given
        final var customerDao = mock(CustomerDao.class);
        when(customerDao.findById(any())).thenReturn(Optional.of(companyExample()));
        final var customerService = new CustomerService(customerDao, mock(MailSender.class));

        // when
        final var updated = customerService.updateAddress(
            new UpdateCustomerAddresss(UUID.randomUUID(), street, zipCode, city, country));

        // then
        assertTrue(updated);
    }

    private Customer companyExample() {
        return Customer.initializeCompanyWith(new RegisterCompanyForm(
            "email@test.com",
            "Test S.A.",
            "0920202033",
            true
        ));
    }
}
