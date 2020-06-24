package io.axoniq.demo.flightbooking.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import io.axoniq.demo.flightbooking.coreapi.BookFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.BookingCancelledEvent;
import io.axoniq.demo.flightbooking.coreapi.CancelBookingCommand;
import io.axoniq.demo.flightbooking.coreapi.CreateFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.FlightBookedEvent;
import io.axoniq.demo.flightbooking.coreapi.FlightCreatedEvent;

@Aggregate
public class Flight {

    @AggregateIdentifier
    private String flightId;

    private int numberOfSeats;

    private final List<String> bookings = new ArrayList<>();

    public Flight() {
    }

    @CommandHandler
    public Flight(CreateFlightCommand command) {
        apply(new FlightCreatedEvent(command.getFlightId(), command.getNumberOfSeats(), command.getOrigin(), command.getDestination()));
    }

    @CommandHandler
    public void handle(BookFlightCommand command) {
        if (bookings.size() >= numberOfSeats) {
            throw new IllegalArgumentException("Seat limit exceeded");
        }
        apply(new FlightBookedEvent(command.getFlightId(), command.getName()));
    }

    @CommandHandler
    public void handle(CancelBookingCommand command) {
        if (!bookings.contains(command.getName())) {
            throw new IllegalArgumentException(("No booking found to cancel"));
        }
        apply(new BookingCancelledEvent(command.getFlightId(), command.getName()));
    }

    @EventSourcingHandler
    public void on(FlightCreatedEvent event) {
        this.flightId = event.getFlightId();
        this.numberOfSeats = event.getNumberOfSeats();
    }

    @EventSourcingHandler
    public void on(FlightBookedEvent event) {
        bookings.add(event.getName());
    }

    @EventSourcingHandler
    public void on(BookingCancelledEvent event) {
        bookings.remove(event.getName());
    }

}
