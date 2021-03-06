package pl.sda.refactoring.customers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class OrderService {

    private CustomerDao customerDao;
    private DiscountCouponsDao couponsDao;
    private OrderDao dao;

    /**
     * Create order and (optionally) apply discount coupon
     * @param cid
     * @param items
     * @param coupon
     * @return
     */
    public boolean makeOrder(UUID cid, List<Item> items, String coupon) {
        var result = false;
        var optional = customerDao.findById(cid);
        if (optional.isPresent() && items != null && items.size() > 0) {
            var order = new Order();
            genId(order);
            order.setCid(cid);
            order.setCtime(LocalDateTime.now());
            order.setStatus(Order.ORDER_STATUS_WAITING);
            var itemsList = order.getItems();
            if (itemsList == null) {
                itemsList = new ArrayList<>();
            }
            itemsList.addAll(items);
            order.setItems(itemsList);

            // add discount
            final var optDiscCoupon = couponsDao.findByCode(coupon);
            if (optDiscCoupon.isPresent()) {
                var dc = optDiscCoupon.get();
                if (!dc.isUsed()) {
                    order.setDiscount(dc.getValue());
                    dc.setUsedBy(cid);
                    dc.setUsed(true);
                    couponsDao.save(dc);
                }
            }

            // calculate delivery
            computeDelivery(items, order);

            var cust = optional.get();

            // save to db and send email
            dao.save(order);
            var sendEmail = sendEmail(cust.getEmail(),
                "Your order is placed!",
                "Thanks for ordering our products. Your order will be send very soon!");
            result = sendEmail;
        }

        return result;
    }

    private void computeDelivery(List<Item> items, Order order) {
        var tp = BigDecimal.ZERO;
        var tw = 0;
        for (Item i : items) {
            tp = tp.add(i.getPrice().multiply(new BigDecimal(i.getQuantity()))); // tp = tp + (i.price * i.quantity)
            tw += (i.getQuantity() * i.getWeight());
        }
        if (tp.compareTo(new BigDecimal(250)) > 0 && tw < 1) {
            order.setDeliveryCost(BigDecimal.ZERO);
        } else if (tw < 1) {
            order.setDeliveryCost(new BigDecimal(15));
        } else if (tw < 5) {
            order.setDeliveryCost(new BigDecimal(35));
        } else {
            order.setDeliveryCost(new BigDecimal(50));
        }
    }

    /**
     * Create order and apply provided discount
     * @param cid
     * @param items
     * @param discount
     * @return
     */
    public boolean makeOrder(UUID cid, List<Item> items, float discount) {
        var result = false;
        var optional = customerDao.findById(cid);
        if (optional.isPresent() && items != null && items.size() > 0 && discount > 0 && discount < 1) {
            var order = new Order();
            genId(order);
            order.setCid(cid);
            order.setCtime(LocalDateTime.now());
            order.setStatus(Order.ORDER_STATUS_WAITING);
            order.setDiscount(discount);
            var itemsList = order.getItems();
            if (itemsList == null) {
                itemsList = new ArrayList<>();
            }
            itemsList.addAll(items);
            order.setItems(itemsList);

            computeDelivery(items, order);

            // save to db
            dao.save(order);

            // send email
            final var sendEmail = sendEmail(optional.get().getEmail(),
                "Your order is placed!",
                "Thanks for ordering our products. Your order will be send very soon!");
            result = sendEmail;
        }

        return result;
    }

    /**
     * Change order status
     * @param oid
     * @param status
     * @return
     */
    public boolean updateOrderStatus(UUID oid, int status) {
        var result = false;
        var optional = dao.findById(oid);
        if (optional.isPresent() && status > 0 && status < 4) {
            var order = optional.get();
            if (status - order.getStatus() == 1) {
                order.setStatus(status);
                dao.save(order);
                var customer = customerDao.findById(order.getCid()).get();
                var emailSend = false;
                if (status == 2) {
                    emailSend = sendEmail(customer.getEmail(),
                        "Order status updated to sent",
                        "Your order changed status to sent. Our courier will deliver your order in 2 business days.");
                } else if (status == 3) {
                    emailSend = sendEmail(customer.getEmail(),
                        "Order status updated to delivered",
                        "Your order changed status to delivered. Thank you for ordering our products!");
                }

                result = emailSend;
            }
        }
        return result;
    }

    private void genId(Order order) {
        order.setId(UUID.randomUUID());
    }

    private boolean sendEmail(String address, String subj, String msg) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", System.getenv().get("MAIL_SMTP_HOST"));
        prop.put("mail.smtp.port", System.getenv().get("MAIL_SMTP_PORT"));
        prop.put("mail.smtp.ssl.trust", System.getenv().get("MAIL_SMTP_SSL_TRUST"));

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "admin");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@company.com"));
            message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(address));
            message.setSubject(subj);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setCouponsDao(DiscountCouponsDao couponsDao) {
        this.couponsDao = couponsDao;
    }

    public void setDao(OrderDao dao) {
        this.dao = dao;
    }
}
