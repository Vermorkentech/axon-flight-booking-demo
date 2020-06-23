package io.axoniq.demo.flightbooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("flightQueryRepository")
public interface FlightRepository extends JpaRepository<Flight, String> {

    List<Flight> findAllByOriginAndDestination(String origin, String destination);
}
