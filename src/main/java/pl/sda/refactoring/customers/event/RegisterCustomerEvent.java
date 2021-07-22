package pl.sda.refactoring.customers.event;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class RegisterCustomerEvent extends Event {

    private final UUID customerId;
    private final String name;
    private final String type;

    public RegisterCustomerEvent(LocalDateTime time, String traceId, EventCreator creator, UUID customerId,
        String name, String type) {
        super(time, traceId, creator);
        this.customerId = customerId;
        this.name = name;
        this.type = type;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        RegisterCustomerEvent that = (RegisterCustomerEvent) o;
        return customerId.equals(that.customerId) && name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId, name, type);
    }
}
