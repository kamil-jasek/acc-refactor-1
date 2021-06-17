package pl.sda.refactoring.customers;

import java.util.Optional;
import java.util.UUID;

interface CustomerDao {

    void save(Customer customer);

    boolean emailExists(String email);

    boolean peselExists(String pesel);

    boolean vatExists(String vat);

    Optional<Customer> findById(UUID cid);
}
