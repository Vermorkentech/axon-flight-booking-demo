package io.axoniq.demo.flightbooking.query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightStatusRepository extends JpaRepository<FlightStatus, String> {

    List<FlightStatus> findAllByOriginAndDestination(String origin, String destination);
}
