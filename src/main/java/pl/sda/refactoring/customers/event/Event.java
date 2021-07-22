package pl.sda.refactoring.customers.event;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Objects;

abstract class Event {

    private final LocalDateTime time;
    private final String traceId;
    private final EventCreator creator;

    protected Event(LocalDateTime time, String traceId, EventCreator creator) {
        this.time = requireNonNull(time);
        this.traceId = requireNonNull(traceId);
        this.creator = requireNonNull(creator);
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTraceId() {
        return traceId;
    }

    public EventCreator getCreator() {
        return creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return time.equals(event.time) && traceId.equals(event.traceId) && creator.equals(event.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, traceId, creator);
    }
}
