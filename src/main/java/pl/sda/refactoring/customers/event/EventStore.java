package pl.sda.refactoring.customers.event;

interface EventStore {

    void push(Event event);

    Event poll();
}
