package io.axoniq.demo.flightbooking.query.flightstatus;

import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.axoniq.demo.flightbooking.coreapi.BookingCancelledEvent;
import io.axoniq.demo.flightbooking.coreapi.FindFlightByIdQuery;
import io.axoniq.demo.flightbooking.coreapi.FindFlightsByRouteQuery;
import io.axoniq.demo.flightbooking.coreapi.FlightBookedEvent;
import io.axoniq.demo.flightbooking.coreapi.FlightCreatedEvent;

@Service
@Profile("query-flight-status")
public class FlightStatusProjection {

    private final FlightStatusRepository repository;

    public FlightStatusProjection(FlightStatusRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(FlightCreatedEvent event) {
        repository.save(new FlightStatus(event.getFlightId(), event.getOrigin(), event.getDestination(), event.getNumberOfSeats(), event.getNumberOfSeats()));
    }

    @EventHandler
    public void on(FlightBookedEvent event) {
        repository.findById(event.getFlightId())
                  .map(flightStatus -> {
                      flightStatus.seatBooked();
                      return flightStatus;
                  });
    }

    @EventHandler
    public void on(BookingCancelledEvent event) {
        repository.findById(event.getFlightId())
                  .map(flightStatus -> {
                      flightStatus.bookingCancelled();
                      return flightStatus;
                  });
    }

    @QueryHandler(queryName = "findAllFlights")
    public List<FlightStatus> handle() {
        return repository.findAll();
    }


    @QueryHandler
    public FlightStatus handle(FindFlightByIdQuery query) {
        return repository.findById(query.getFlightId()).orElse(null);
    }

    @QueryHandler
    public List<FlightStatus> handle(FindFlightsByRouteQuery query) {
        return repository.findAllByOriginAndDestination(query.getOrigin(), query.getDestination());
    }
}
