package io.axoniq.demo.flightbooking.query.passengers;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("query-passenger-manifest")
public interface PassengerManifestRepository extends JpaRepository<PassengerManifest, String> {
}
