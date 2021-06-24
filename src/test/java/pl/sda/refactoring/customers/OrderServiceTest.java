package pl.sda.refactoring.customers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.mail.Transport;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

public class OrderServiceTest {

    @Test
    public void testMakeOrderWithoutCoupon() throws Exception {
        TestUtil.setEnv(Map.of(
            "MAIL_SMTP_HOST", "smtp",
            "MAIL_SMTP_PORT", "27",
            "MAIL_SMTP_SSL_TRUST", "true"
        ));
        var mailTransport = mockStatic(Transport.class);
        var dao = mock(OrderDao.class);
        var customerDao = mock(CustomerDao.class);
        var couponDao = mock(DiscountCouponsDao.class);
        var service = new OrderService();
        service.setDao(dao);
        service.setCustomerDao(customerDao);
        service.setCouponsDao(couponDao);

        // some customer
        var cid = UUID.randomUUID();
        var customer = new Customer();
        customer.setId(cid);
        customer.setCompName("Test S.A.");
        customer.setCompVat("12020303");
        customer.setEmail("email@email.com");
        when(customerDao.findById(any())).thenReturn(Optional.of(customer));

        // some product item
        var item = new Item();
        item.setName("Product 1");
        item.setPrice(new BigDecimal("200.00"));
        item.setQuantity(2);
        item.setWeight(1.25F);

        var orderCapture = ArgumentCaptor.forClass(Order.class);

        // test it
        final var result = service.makeOrder(cid, List.of(item), "test");
        verify(dao).save(orderCapture.capture());
        var order = orderCapture.getValue();
        assertTrue(result);
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("35"), order.getDeliveryCost());
        assertEquals(0F, order.getDiscount());
        mailTransport.verify(() -> Transport.send(any()));
        mailTransport.close();
    }

    @Test
    public void testMakeOrderWithCoupon() throws Exception {
        TestUtil.setEnv(Map.of(
            "MAIL_SMTP_HOST", "smtp",
            "MAIL_SMTP_PORT", "27",
            "MAIL_SMTP_SSL_TRUST", "true"
        ));
        var mailTransport = mockStatic(Transport.class);
        var dao = mock(OrderDao.class);
        var customerDao = mock(CustomerDao.class);
        var couponDao = mock(DiscountCouponsDao.class);
        var service = new OrderService();
        service.setDao(dao);
        service.setCustomerDao(customerDao);
        service.setCouponsDao(couponDao);

        // some customer
        var cid = UUID.randomUUID();
        var customer = new Customer();
        customer.setId(cid);
        customer.setCompName("Test S.A.");
        customer.setCompVat("12020303");
        customer.setEmail("email@email.com");
        when(customerDao.findById(any())).thenReturn(Optional.of(customer));

        var dc = new DiscountCoupon();
        dc.setValue(0.25F);
        when(couponDao.findByCode(any())).thenReturn(Optional.of(dc));

        // some product item
        var item = new Item();
        item.setName("Product 1");
        item.setPrice(new BigDecimal("200.00"));
        item.setQuantity(2);
        item.setWeight(1.25F);

        var orderCapture = ArgumentCaptor.forClass(Order.class);

        // test it
        final var result = service.makeOrder(cid, List.of(item), "test");
        verify(dao).save(orderCapture.capture());
        var order = orderCapture.getValue();
        assertTrue(result);
        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("35"), order.getDeliveryCost());
        assertEquals(0.25F, order.getDiscount());
        mailTransport.verify(() -> Transport.send(any()));
        mailTransport.close();
    }

    @Test
    public void testCustomerNotFound() {
        var mailTransport = mockStatic(Transport.class);
        var customerDao = mock(CustomerDao.class);
        var service = new OrderService();
        service.setCustomerDao(customerDao);
        when(customerDao.findById(any())).thenReturn(Optional.empty());

        var item = new Item();
        item.setName("Product 1");
        item.setPrice(new BigDecimal("200.00"));
        item.setQuantity(2);
        item.setWeight(1.25F);

        // test it
        final var result = service.makeOrder(UUID.randomUUID(), List.of(item), "test");
        assertFalse(result);
        mailTransport.verifyNoInteractions();
        mailTransport.close();
    }
}
