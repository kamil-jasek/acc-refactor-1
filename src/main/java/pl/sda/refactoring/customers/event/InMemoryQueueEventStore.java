package pl.sda.refactoring.customers.event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

final class InMemoryQueueEventStore implements EventStore {

    private final BlockingQueue<Event> queue;

    InMemoryQueueEventStore() {
        this.queue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);
    }

    @Override
    public void push(Event event) {
        queue.offer(event);
    }

    @Override
    public Event poll() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
