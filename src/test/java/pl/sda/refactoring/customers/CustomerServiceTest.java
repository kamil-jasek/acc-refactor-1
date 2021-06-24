package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.sda.refactoring.customers.exception.CompanyAlreadyExistsException;
import pl.sda.refactoring.customers.exception.InvalidCompanyCustomerException;
import pl.sda.refactoring.customers.exception.RegisterFormNotFilledException;

public class CustomerServiceTest {

    @Test
    public void shouldRegisterVerifiedCompany() throws Exception {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var customerCapture = ArgumentCaptor.forClass(Customer.class);
        var mailSender = mock(MailSender.class);
        when(mailSender.sendEmail(anyString(), anyString(), anyString())).thenReturn(true);
        var service = new CustomerService(dao, mailSender);

        // when
        var reg = service.registerCompany(new RegisterCompanyForm("test@test.com", "Test S.A.", "9302030403", true));

        // then
        assertTrue(reg);
        verify(dao).save(customerCapture.capture());
        var customer = customerCapture.getValue();
        assertNotNull(customer.getId());
        assertEquals(customer.getEmail(), "test@test.com");
        assertEquals(customer.getCompName(), "Test S.A.");
        assertEquals(customer.getCompVat(), "9302030403");
        assertTrue(customer.isVerf());
        assertEquals(customer.getVerifBy(), CustomerVerifier.AUTO_EMAIL);
        assertNotNull(customer.getVerfTime());
    }

    @Test
    public void shouldNotRegisterCompanyIfAlreadyExists() throws Exception {
        // given
        final var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(true);
        when(dao.vatExists(anyString())).thenReturn(true);
        final var mailSender = mock(MailSender.class);
        final var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(CompanyAlreadyExistsException.class, () -> service.registerCompany(
            new RegisterCompanyForm("mail@comp.com", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void shouldNotRegisterCompanyIfFormIsNotFilled() {
        // given
        final var dao = mock(CustomerDao.class);
        final var mailSender = mock(MailSender.class);
        final var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(RegisterFormNotFilledException.class, () -> service.registerCompany(
            new RegisterCompanyForm(null, null, null, false)));
    }

    @Test
    public void testEmailFail() throws Exception {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var mailSender = mock(MailSender.class);
        var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(InvalidCompanyCustomerException.class, () -> service.registerCompany(
            new RegisterCompanyForm("invalid@", "Test S.A.", "9302030403", true)));
    }

    @Test
    public void testNameFail() throws Exception {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var mailSender = mock(MailSender.class);
        var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(InvalidCompanyCustomerException.class, () -> service.registerCompany(
            new RegisterCompanyForm("test@ok.com", "F&", "9302030403", true)));
    }

    @Test
    public void testVatFail() throws Exception {
        // given
        var dao = mock(CustomerDao.class);
        when(dao.emailExists(anyString())).thenReturn(false);
        when(dao.vatExists(anyString())).thenReturn(false);
        var mailSender = mock(MailSender.class);
        var service = new CustomerService(dao, mailSender);

        // when
        assertThrows(InvalidCompanyCustomerException.class, () -> service.registerCompany(
            new RegisterCompanyForm("test@ok.com", "TestOK", "AB02030403", true)));
    }
}
