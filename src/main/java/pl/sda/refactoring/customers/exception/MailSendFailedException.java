package pl.sda.refactoring.customers.exception;

final public class MailSendFailedException extends DomainException {

    public MailSendFailedException(String message) {
        super(message);
    }

    public MailSendFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
