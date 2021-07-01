package pl.sda.refactoring.customers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class TestCustomerDao implements CustomerDao {

    private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

    @Override
    public void save(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    @Override
    public boolean emailExists(String email) {
        return customers.values()
            .stream()
            .anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean peselExists(String pesel) {
        return customers.values()
            .stream()
            .anyMatch(customer -> customer.getPesel().equals(pesel));
    }

    @Override
    public boolean vatExists(String vat) {
        return customers.values()
            .stream()
            .anyMatch(customer -> customer.getCompVat().equals(vat));
    }

    @Override
    public Optional<Customer> findById(UUID customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    public Customer getCompanyById(UUID customerId) {
        return customers.get(customerId);
    }
}
