package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

public class EventCreator {

    public enum Type {
        USER, ADMIN, SYSTEM
    }

    private final String name;
    private final Type typ;

    public EventCreator(String name, Type typ) {
        this.name = requireNonNull(name);
        this.typ = requireNonNull(typ);
    }

    public String getName() {
        return name;
    }

    public Type getTyp() {
        return typ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventCreator that = (EventCreator) o;
        return name.equals(that.name) && typ == that.typ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typ);
    }
}
