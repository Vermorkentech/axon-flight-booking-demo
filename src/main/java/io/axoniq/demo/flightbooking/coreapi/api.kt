package io.axoniq.demo.flightbooking.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class CreateFlightCommand(@TargetAggregateIdentifier val flightId: String, val numberOfSeats: Int, val origin: String, val destination: String)
data class BookFlightCommand(@TargetAggregateIdentifier val flightId: String, val name: String)
data class CancelBookingCommand(@TargetAggregateIdentifier val flightId: String, val name: String)

data class FindFlightByIdQuery(val flightId: String)
data class FindFlightsByRouteQuery(val origin: String, val destination: String)

data class FindPassengerManifestQuery(val flightId: String)

data class FlightCreatedEvent(val flightId: String, val numberOfSeats: Int, val origin: String, val destination: String)
data class FlightBookedEvent(val flightId: String, val name: String)
data class BookingCancelledEvent(val flightId: String, val name: String)