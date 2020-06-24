package io.axoniq.demo.flightbooking.query.flightstatus;

import java.util.List;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import io.axoniq.demo.flightbooking.coreapi.BookingCancelledEvent;
import io.axoniq.demo.flightbooking.coreapi.FindFlightByIdQuery;
import io.axoniq.demo.flightbooking.coreapi.FindFlightsByRouteQuery;
import io.axoniq.demo.flightbooking.coreapi.FlightBookedEvent;
import io.axoniq.demo.flightbooking.coreapi.FlightCreatedEvent;

@Service
public class FlightStatusProjection {

    private final FlightStatusRepository repository;
    private final QueryUpdateEmitter updateEmitter;

    public FlightStatusProjection(FlightStatusRepository repository, QueryUpdateEmitter updateEmitter) {
        this.repository = repository;
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    public void on(FlightCreatedEvent event) {
        repository.save(new FlightStatus(event.getFlightId(), event.getOrigin(), event.getDestination(), event.getNumberOfSeats(), event.getNumberOfSeats()));
    }

    @EventHandler
    public void on(FlightBookedEvent event) {
        FlightStatus newStatus = repository.findById(event.getFlightId())
                                           .map(flightStatus -> {
                                               flightStatus.setAvailableSeats(flightStatus.getAvailableSeats() - 1);
                                               return flightStatus;
                                           }).get();
        updateEmitter.emit(FindFlightByIdQuery.class,
                           query -> query.getFlightId().equals(newStatus.getFlightId()),
                           newStatus);
    }

    @EventHandler
    public void on(BookingCancelledEvent event) {
        FlightStatus newStatus = repository.findById(event.getFlightId())
                                           .map(flightStatus -> {
                                               flightStatus.setAvailableSeats(flightStatus.getAvailableSeats() + 1);
                                               return flightStatus;
                                           }).get();
        updateEmitter.emit(FindFlightByIdQuery.class,
                           query -> query.getFlightId().equals(newStatus.getFlightId()),
                           newStatus);
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
