package io.axoniq.demo.flightbooking.query.flightstatus;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("query-flight-status")
public interface FlightStatusRepository extends JpaRepository<FlightStatus, String> {

    List<FlightStatus> findAllByOriginAndDestination(String origin, String destination);
}
