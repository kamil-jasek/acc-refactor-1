package pl.sda.refactoring.customers;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import pl.sda.refactoring.customers.exception.MailSendFailedException;

final class MailCustomerNotifier implements CustomerNotifier {

    @Override
    public void notifyRegistration(RegistrationNotification notification) {
        try {
            String subject;
            String body;
            if (notification.isVerified()) {
                subject = "Your are now verified customer!";
                body = "<b>Hi " + notification.getName() + "</b><br/>" +
                    "Thank you for registering in our service. Now you are verified customer!";
            } else {
                subject = "Waiting for verification";
                body = "<b>Hi " + notification.getName() + "</b><br/>" +
                    "We registered you in our service. Please wait for verification!";
            }

            Transport.send(buildMailMesssage(notification, subject, body));
        } catch (Exception ex) {
            throw new MailSendFailedException(ex.getMessage(), ex);
        }
    }

    private Message buildMailMesssage(RegistrationNotification notification, String subject, String body)
        throws MessagingException {
        Message message = new MimeMessage(buildMailSession());
        message.setFrom(new InternetAddress("no-reply@company.com"));
        message.setRecipients(
            Message.RecipientType.TO, InternetAddress.parse(notification.getEmail()));
        message.setSubject(subject);
        message.setContent(buildMultipart(body));
        return message;
    }

    private Multipart buildMultipart(String body) throws MessagingException {
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(body, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        return multipart;
    }

    private Session buildMailSession() {
        Session session = Session.getInstance(buildProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "admin");
            }
        });
        return session;
    }

    private Properties buildProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", System.getenv().get("MAIL_SMTP_HOST"));
        prop.put("mail.smtp.port", System.getenv().get("MAIL_SMTP_PORT"));
        prop.put("mail.smtp.ssl.trust", System.getenv().get("MAIL_SMTP_SSL_TRUST"));
        return prop;
    }
}
