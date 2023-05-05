package com.joranbergfeld.airportsystem.flightscheduler;

import com.joranbergfeld.airport_system.flight.client.model.Flight;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flight-scheduler")
public class FlightSchedulerController {

    private final FlightSchedulerService flightSchedulerService;

    public FlightSchedulerController(FlightSchedulerService flightSchedulerService) {
        this.flightSchedulerService = flightSchedulerService;
    }

    @PostMapping("/random")
    public Flight generateRandomFlight() {
        return flightSchedulerService.generateRandomFlightSchedule();
    }
}
