package io.axoniq.demo.flightbooking;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.axoniq.demo.flightbooking.coreapi.BookFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.CancelBookingCommand;
import io.axoniq.demo.flightbooking.coreapi.CreateFlightCommand;
import io.axoniq.demo.flightbooking.coreapi.FindFlightByIdQuery;
import io.axoniq.demo.flightbooking.coreapi.FindFlightsByRouteQuery;
import io.axoniq.demo.flightbooking.coreapi.FindPassengerManifestQuery;
import io.axoniq.demo.flightbooking.query.flightstatus.FlightStatus;
import io.axoniq.demo.flightbooking.query.passengers.PassengerManifest;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public FlightController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public CompletableFuture<String> createFlight(@RequestBody CreateFlightCommand command) {
        return commandGateway.send(command);
    }

    @PostMapping("/{flightId}/book")
    public CompletableFuture<String> bookFlight(@PathVariable String flightId,
                                                @RequestParam("name") String name) {
        return commandGateway.send(new BookFlightCommand(flightId, name))
                             .thenApply(o -> "Flight booked successfully")
                             .exceptionally(Throwable::getMessage);
    }

    @DeleteMapping("/{flightId}/cancel")
    public CompletableFuture<String> cancelBooking(@PathVariable String flightId,
                                                   @RequestParam("name") String name) {
        return commandGateway.send(new CancelBookingCommand(flightId, name))
                             .thenApply(o -> "Booking cancelled")
                             .exceptionally(Throwable::getMessage);
    }

    @GetMapping
    public CompletableFuture<List<FlightStatus>> findAll() {
        return queryGateway.query("findAllFlights", null, ResponseTypes.multipleInstancesOf(FlightStatus.class));
    }

    @GetMapping("/{flightId}")
    public CompletableFuture<FlightStatus> findById(@PathVariable String flightId) {
        return queryGateway.query(new FindFlightByIdQuery(flightId), FlightStatus.class);
    }

    @GetMapping("/getByRoute")
    public CompletableFuture<List<FlightStatus>> findFlightsByRoute(@RequestParam("origin") String origin,
                                                                    @RequestParam("destination") String destination) {
        return queryGateway.query(new FindFlightsByRouteQuery(origin, destination), ResponseTypes.multipleInstancesOf(FlightStatus.class));
    }

    @GetMapping("/{flightId}/passengers")
    public CompletableFuture<PassengerManifest> findPassengerManifest(@PathVariable String flightId) {
        return queryGateway.query(new FindPassengerManifestQuery(flightId), PassengerManifest.class);
    }

    @GetMapping(value = "/{flightId}/watch", produces = "text/event-stream")
    public Flux<FlightStatus> watchFlight(@PathVariable String flightId) {
        SubscriptionQueryResult<FlightStatus, FlightStatus> subscriptionQueryResult =
                queryGateway.subscriptionQuery(new FindFlightByIdQuery(flightId), FlightStatus.class, FlightStatus.class);
        return subscriptionQueryResult
                .initialResult()
                .mergeWith(subscriptionQueryResult.updates());
    }

}
