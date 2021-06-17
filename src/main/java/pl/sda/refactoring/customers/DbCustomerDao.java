package pl.sda.refactoring.customers;

import java.util.Optional;
import java.util.UUID;

public class DbCustomerDao implements CustomerDao {

    @Override
    public void save(Customer customer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean emailExists(String email) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean peselExists(String pesel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean vatExists(String vat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Customer> findById(UUID cid) {
        throw new UnsupportedOperationException();
    }
}
