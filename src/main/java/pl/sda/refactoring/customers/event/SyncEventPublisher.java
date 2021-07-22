package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class SyncEventPublisher implements EventPublisher {

    private final EventStore eventStore;
    private final List<EventListener> listeners;
    private final ExecutorService executorService;

    public SyncEventPublisher(EventStore eventStore) {
        this.eventStore = requireNonNull(eventStore);
        this.listeners = new ArrayList<>();
        this.executorService = Executors.newSingleThreadExecutor();
        run();
    }

    @Override
    public void addListener(EventListener eventListener) {
        if (!listeners.contains(eventListener)) {
            listeners.add(eventListener);
        }
    }

    @Override
    public void removeListener(EventListener eventListener) {
        listeners.remove(eventListener);
    }

    @Override
    public void publish(Event event) {
        eventStore.push(event);
    }

    private void run() {
        executorService.submit(() -> {
            Event event;
            while ((event = eventStore.poll()) != null) {
                processEvent(event);
            }
        });
    }

    private void processEvent(Event event) {
        for (final var listener: listeners) {
            if (listener.supports(event.getClass())) {
                tryProcessEvent(event, listener);
            }
        }
    }

    private void tryProcessEvent(Event event, EventListener listener) {
        try {
            listener.process(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
