package io.axoniq.demo.flightbooking;

import io.axoniq.demo.flightbooking.coreapi.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<List<Flight>> findAll() {
        return queryGateway.query("findAllFlights", null, ResponseTypes.multipleInstancesOf(Flight.class));
    }

    @GetMapping("/{flightId}")
    public CompletableFuture<Flight> findById(@PathVariable String flightId) {
        return queryGateway.query(new FindFlightByIdQuery(flightId), Flight.class);
    }

    @GetMapping("/getByRoute")
    public CompletableFuture<List<Flight>> findFlightsByRoute(@RequestParam("origin") String origin,
                                                              @RequestParam("destination") String destination) {
        return queryGateway.query(new FindFlightsByRouteQuery(origin, destination), ResponseTypes.multipleInstancesOf(Flight.class));
    }

}
