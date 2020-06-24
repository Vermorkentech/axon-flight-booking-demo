package io.axoniq.demo.flightbooking.query.flightstatus;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.context.annotation.Profile;

@Entity
@Profile("query-flight-status")
public class FlightStatus {

    @Id
    private String flightId;
    private String origin;
    private String destination;
    private int numberOfSeats;
    private int availableSeats;

    public FlightStatus() {

    }

    public FlightStatus(String flightId, String origin, String destination, int numberOfSeats, int availableSeats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.numberOfSeats = numberOfSeats;
        this.availableSeats = availableSeats;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
