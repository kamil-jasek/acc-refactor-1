package pl.sda.refactoring.customers.event;

public interface EventPublisher {

    void addListener(EventListener eventListener);

    void removeListener(EventListener eventListener);

    void publish(Event event);
}
