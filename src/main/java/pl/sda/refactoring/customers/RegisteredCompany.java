package pl.sda.refactoring.customers;

import java.util.UUID;

final class RegisteredCompany {

    private final UUID id;

    public RegisteredCompany(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
