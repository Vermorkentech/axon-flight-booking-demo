package io.axoniq.demo.flightbooking.query.passengers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerManifestRepository extends JpaRepository<PassengerManifest, String> {
}
