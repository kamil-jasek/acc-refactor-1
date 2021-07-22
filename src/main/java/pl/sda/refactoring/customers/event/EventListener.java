package pl.sda.refactoring.customers.event;

interface EventListener {

    boolean supports(Class<? extends Event> event);

    void process(Event event);
}
