package io.axoniq.demo.flightbooking;

import io.axoniq.demo.flightbooking.coreapi.BookFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.CancelBookingCommand;
import io.axoniq.demo.flightbooking.coreapi.CreateFlightCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Aggregate
public class Flight {

    @Id
    private String flightId;

    @Basic(fetch = FetchType.EAGER)
    private int numberOfSeats;

    @Basic(fetch = FetchType.EAGER)
    private int availableSeats;

    @Basic
    private String origin;

    @Basic
    private String destination;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<String> bookings = new ArrayList<>();

    public Flight() {
    }

    @CommandHandler
    public Flight(CreateFlightCommand command) {
        this.flightId = command.getFlightId();
        this.numberOfSeats = command.getNumberOfSeats();
        this.availableSeats = command.getNumberOfSeats();
        this.origin = command.getOrigin();
        this.destination = command.getDestination();
    }

    @CommandHandler
    public void handle(BookFlightCommand command) {
        if (availableSeats <= 0) {
            throw new IllegalArgumentException("Seat limit exceeded");
        }
        bookings.add(command.getName());
        availableSeats--;
    }

    @CommandHandler
    public void handle(CancelBookingCommand command) {
        if (!bookings.contains(command.getName())) {
            throw new IllegalArgumentException(("No booking found to cancel"));
        }
        bookings.remove(command.getName());
        availableSeats++;
    }

    public String getFlightId() {
        return flightId;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getBookings() {
        return bookings;
    }
}
