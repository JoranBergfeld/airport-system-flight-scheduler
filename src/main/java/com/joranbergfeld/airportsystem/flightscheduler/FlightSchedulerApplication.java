package com.joranbergfeld.airportsystem.flightscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = {"com.joranbergfeld.airportsystem.flightscheduler"})
public class FlightSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightSchedulerApplication.class, args);
    }

}
