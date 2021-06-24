package pl.sda.refactoring.customers.exception;

public final class CompanyAlreadyExistsException extends DomainException {

    public CompanyAlreadyExistsException(String msg) {
        super(msg);
    }
}
