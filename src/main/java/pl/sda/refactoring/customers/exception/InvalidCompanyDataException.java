package pl.sda.refactoring.customers.exception;

public final class InvalidCompanyDataException extends DomainException {

    public InvalidCompanyDataException(String msg) {
        super(msg);
    }
}
