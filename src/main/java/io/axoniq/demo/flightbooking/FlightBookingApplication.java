package io.axoniq.demo.flightbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

@SpringBootApplication
public class FlightBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new KotlinModule());
    }
}
