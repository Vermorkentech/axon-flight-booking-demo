package io.axoniq.demo.flightbooking.query.passengers;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.axoniq.demo.flightbooking.coreapi.BookingCancelledEvent;
import io.axoniq.demo.flightbooking.coreapi.FindPassengerManifestQuery;
import io.axoniq.demo.flightbooking.coreapi.FlightBookedEvent;
import io.axoniq.demo.flightbooking.coreapi.FlightCreatedEvent;

@Component
@Profile("query-passenger-manifest")
public class PassengerManifestProjection {

    private final PassengerManifestRepository repository;

    public PassengerManifestProjection(PassengerManifestRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(FlightCreatedEvent event) {
        repository.save(new PassengerManifest(event.getFlightId()));
    }

    @EventHandler
    public void on(FlightBookedEvent event) {
        repository.findById(event.getFlightId())
                  .map(passengerManifest -> {
                      passengerManifest.addPassenger(event.getName());
                      return passengerManifest;
                  });
    }

    @EventHandler
    public void on(BookingCancelledEvent event) {
        repository.findById(event.getFlightId())
                  .map(passengerManifest -> {
                      passengerManifest.removePassenger(event.getName());
                      return passengerManifest;
                  });
    }

    @QueryHandler
    public PassengerManifest handle(FindPassengerManifestQuery query) {
        return repository.findById(query.getFlightId()).orElse(null);
    }
}
