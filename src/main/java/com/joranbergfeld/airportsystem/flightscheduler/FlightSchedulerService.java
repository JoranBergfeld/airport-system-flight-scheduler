package com.joranbergfeld.airportsystem.flightscheduler;


import com.joranbergfeld.airport_system.airliner.client.api.AirlinerControllerApi;
import com.joranbergfeld.airport_system.airliner.client.model.Airliner;
import com.joranbergfeld.airport_system.flight.client.api.FlightControllerApi;
import com.joranbergfeld.airport_system.flight.client.model.Flight;
import com.joranbergfeld.airport_system.gate.client.api.GateControllerApi;
import com.joranbergfeld.airport_system.gate.client.model.Gate;
import com.joranbergfeld.airport_system.plane.client.api.PlaneControllerApi;
import com.joranbergfeld.airport_system.plane.client.model.Plane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class FlightSchedulerService {

    private final AirlinerControllerApi airlinerClient;
    private final PlaneControllerApi planeClient;
    private final GateControllerApi gateClient;
    private final FlightControllerApi flightClient;

    private final AppConfigProperties properties;

    private final Logger log = LoggerFactory.getLogger(FlightSchedulerService.class);

    public FlightSchedulerService(AirlinerControllerApi airlinerClient, PlaneControllerApi planeClient, GateControllerApi gateClient, FlightControllerApi flightClient, AppConfigProperties properties) {
        this.airlinerClient = airlinerClient;
        this.planeClient = planeClient;
        this.gateClient = gateClient;
        this.flightClient = flightClient;
        this.properties = properties;
    }

    public Flight generateRandomFlightSchedule() {
        List<Airliner> airliners = airlinerClient.getAllAirliners();
        List<Plane> planes = planeClient.getAllPlanes();
        List<Gate> gates = gateClient.getAllGates();
        List<Flight> flights = flightClient.getAllFlights();

        // Shuffle the lists to randomize the selection process
        Collections.shuffle(airliners);
        Collections.shuffle(planes);
        Collections.shuffle(gates);

        List<Gate> unoccupiedGates = flights.stream().filter(flight -> {
            Instant gateAvailableAt = Instant.ofEpochSecond(flight.getTaxiTime()).plus(properties.getScheduling().getGracePeriodAfterGate(), ChronoUnit.MINUTES);
            return gateAvailableAt.isBefore(Instant.now());
        }).map(flight -> gates.stream().filter(gate -> Objects.equals(gate.getId(), flight.getGateId())).findFirst().get()).toList();

        Plane plane = obtainRandomPlane(planes);
        Gate gate = obtainRandomGate(unoccupiedGates);
        Airliner airliner = obtainRandomAirliner(airliners);

        log.info("Scheduling new flight at gate " + gate.getId() + ", operated by " + airliner.getName() + ", utilizing plane " + plane.getName());
        Flight flight = new Flight();
        flight.setGateId(gate.getId());
        flight.setAirlinerId(airliner.getId());
        flight.setPlaneId(plane.getId());
        flight.setActive(true);
        flight.setPlaneAtGateTime(Instant.now().getEpochSecond());
        flight.setBoardingTime(Instant.now().plus(45, ChronoUnit.MINUTES).getEpochSecond());
        flight.setTaxiTime(Instant.now().plus(30 + 45, ChronoUnit.MINUTES).getEpochSecond());
        return flightClient.createFlight(flight);
    }

    private Airliner obtainRandomAirliner(List<Airliner> airliners) {
        try {
            Random rand = new Random();
            return airliners.get(rand.nextInt(airliners.size()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine random gate.", e);
        }
    }

    private Gate obtainRandomGate(List<Gate> gates) {
        try {
            Random rand = new Random();
            return gates.get(rand.nextInt(gates.size()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine random gate.", e);
        }
    }

    private Plane obtainRandomPlane(List<Plane> planes) {
        try {
            Random rand = new Random();
            return planes.get(rand.nextInt(planes.size()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine random plane.", e);
        }
    }
}
