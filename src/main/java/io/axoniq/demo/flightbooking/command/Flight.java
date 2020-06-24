package io.axoniq.demo.flightbooking.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import io.axoniq.demo.flightbooking.coreapi.BookFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.BookingCancelledEvent;
import io.axoniq.demo.flightbooking.coreapi.CancelBookingCommand;
import io.axoniq.demo.flightbooking.coreapi.CreateFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.FlightBookedEvent;
import io.axoniq.demo.flightbooking.coreapi.FlightCreatedEvent;

@Entity
@Aggregate
public class Flight {

    @Id
    private String flightId;

    @Basic(fetch = FetchType.EAGER)
    private int numberOfSeats;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<String> bookings = new ArrayList<>();

    public Flight() {
    }

    @CommandHandler
    public Flight(CreateFlightCommand command) {
        this.flightId = command.getFlightId();
        this.numberOfSeats = command.getNumberOfSeats();
        apply(new FlightCreatedEvent(command.getFlightId(), command.getNumberOfSeats(), command.getOrigin(), command.getDestination()));
    }

    @CommandHandler
    public void handle(BookFlightCommand command) {
        if (bookings.size() >= numberOfSeats) {
            throw new IllegalArgumentException("Seat limit exceeded");
        }
        bookings.add(command.getName());
        apply(new FlightBookedEvent(command.getFlightId(), command.getName()));
    }

    @CommandHandler
    public void handle(CancelBookingCommand command) {
        if (!bookings.contains(command.getName())) {
            throw new IllegalArgumentException(("No booking found to cancel"));
        }
        bookings.remove(command.getName());
        apply(new BookingCancelledEvent(command.getFlightId(), command.getName()));
    }

}
