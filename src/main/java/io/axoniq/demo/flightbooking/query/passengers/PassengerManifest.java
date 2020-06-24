package io.axoniq.demo.flightbooking.query.passengers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

@Entity
public class PassengerManifest {

    @Id
    private String flightId;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<String> passengers = new ArrayList<>();

    public PassengerManifest() { }

    public PassengerManifest(String flightId) {
        this.flightId = flightId;
    }

    public void addPassenger(String name) {
        passengers.add(name);
    }

    public void removePassenger(String name) {
        passengers.remove(name);
    }

    public String getFlightId() {
        return flightId;
    }

    public List<String> getPassengers() {
        return passengers;
    }
}
