package io.axoniq.demo.flightbooking;

import io.axoniq.demo.flightbooking.coreapi.FindFlightByIdQuery;
import io.axoniq.demo.flightbooking.coreapi.FindFlightsByRouteQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightQueryService {

    private final FlightRepository repository;

    public FlightQueryService(@Qualifier("flightQueryRepository") FlightRepository repository) {
        this.repository = repository;
    }

    @QueryHandler(queryName = "findAllFlights")
    public List<Flight> handle() {
        return repository.findAll();
    }


    @QueryHandler
    public Flight handle(FindFlightByIdQuery query) {
        return repository.findById(query.getFlightId()).orElse(null);
    }

    @QueryHandler
    public List<Flight> handle(FindFlightsByRouteQuery query) {
        return repository.findAllByOriginAndDestination(query.getOrigin(), query.getDestination());
    }
}
