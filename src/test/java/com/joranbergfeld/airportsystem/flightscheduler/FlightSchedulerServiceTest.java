package com.joranbergfeld.airportsystem.flightscheduler;

import com.joranbergfeld.airport_system.airliner.client.api.AirlinerControllerApi;
import com.joranbergfeld.airport_system.airliner.client.model.Airliner;
import com.joranbergfeld.airport_system.flight.client.api.FlightControllerApi;
import com.joranbergfeld.airport_system.flight.client.model.Flight;
import com.joranbergfeld.airport_system.gate.client.api.GateControllerApi;
import com.joranbergfeld.airport_system.gate.client.model.Gate;
import com.joranbergfeld.airport_system.plane.client.api.PlaneControllerApi;
import com.joranbergfeld.airport_system.plane.client.model.Plane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightSchedulerServiceTest {

    private final AirlinerControllerApi airlinerControllerApi = Mockito.mock(AirlinerControllerApi.class);
    private final PlaneControllerApi planeControllerApi = Mockito.mock(PlaneControllerApi.class);
    private final GateControllerApi gateControllerApi = Mockito.mock(GateControllerApi.class);
    private final FlightControllerApi flightControllerApi = Mockito.mock(FlightControllerApi.class);
    private final AppConfigProperties properties = Mockito.mock(AppConfigProperties.class);
    private final FlightSchedulerService schedulerService = new FlightSchedulerService(airlinerControllerApi, planeControllerApi, gateControllerApi, flightControllerApi, properties);

    @BeforeEach
    void setUp() {
        Gate gate1 = new Gate();
        gate1.setActive(true);
        gate1.setId(1234L);
        gate1.setName("gate1");
        gate1.setSize(10);

        Gate gate2 = new Gate();
        gate2.setActive(true);
        gate2.setId(5678L);
        gate2.setName("gate2");
        gate2.setSize(20);

        ArrayList<Gate> gates = new ArrayList<>();
        gates.add(gate1);
        gates.add(gate2);
        Mockito.when(gateControllerApi.getAllGates()).thenReturn(gates);

        Plane plane1 = new Plane();
        plane1.setActive(true);
        plane1.setId(1234L);
        plane1.setName("plane1");
        plane1.setCrewRequired(12);
        plane1.setPassengerCapacity(120);

        ArrayList<Plane> planes = new ArrayList<>();
        planes.add(plane1);

        Mockito.when(planeControllerApi.getAllPlanes()).thenReturn(planes);

        Airliner airliner1 = new Airliner();
        airliner1.setActive(true);
        airliner1.setId(1234L);
        airliner1.setName("airliner1");
        airliner1.setCountry("country1");

        ArrayList<Airliner> airliners = new ArrayList<>();
        airliners.add(airliner1);

        Mockito.when(airlinerControllerApi.getAllAirliners()).thenReturn(airliners);
    }

    @DisplayName("Testing when there are no flights scheduled, the scheduling should be completed successfully.")
    @Test
    void testHappyFlow() {
        Flight scheduledFlight = schedulerService.generateRandomFlightSchedule();

        Mockito.verify(airlinerControllerApi, Mockito.times(1)).getAllAirliners();
        Mockito.verify(planeControllerApi, Mockito.times(1)).getAllPlanes();
        Mockito.verify(gateControllerApi, Mockito.times(1)).getAllGates();
        Mockito.verify(flightControllerApi, Mockito.times(1)).getAllFlights();

        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightControllerApi, Mockito.times(1)).createFlight(flightArgumentCaptor.capture());
    }

    @DisplayName("Testing when there is already a flight scheduled, but that has a taxi time of 60 minutes ago. Scheduling should be possible.")
    @Test
    void testSchedulingWhenSomeFlightsHaveAlreadyBeenScheduled() {
        Flight flight1 = new Flight();
        flight1.setActive(true);
        flight1.setId(1234L);
        flight1.setAirlinerId(1234L);
        flight1.setGateId(1234L);
        flight1.setPlaneId(1234L);
        flight1.setTaxiTime(Instant.now().minus(60, ChronoUnit.MINUTES).getEpochSecond());

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(flight1);

        Mockito.when(flightControllerApi.getAllFlights()).thenReturn(flights);

        AppConfigProperties.Scheduling scheduling = new AppConfigProperties.Scheduling();
        scheduling.setGracePeriodAfterGate(15);
        Mockito.when(properties.getScheduling()).thenReturn(scheduling);

        Flight scheduledFlight = schedulerService.generateRandomFlightSchedule();

        Mockito.verify(airlinerControllerApi, Mockito.times(1)).getAllAirliners();
        Mockito.verify(planeControllerApi, Mockito.times(1)).getAllPlanes();
        Mockito.verify(gateControllerApi, Mockito.times(1)).getAllGates();
        Mockito.verify(flightControllerApi, Mockito.times(1)).getAllFlights();

        ArgumentCaptor<Flight> flightArgumentCaptor = ArgumentCaptor.forClass(Flight.class);
        Mockito.verify(flightControllerApi, Mockito.times(1)).createFlight(flightArgumentCaptor.capture());
    }

    @DisplayName("Testing when there's already a flight on both available gates.")
    @Test
    void testSchedulingWithCorrespondingFlight() {
        Flight flight1 = new Flight();
        flight1.setActive(true);
        flight1.setId(1234L);
        flight1.setAirlinerId(1234L);
        flight1.setGateId(1234L);
        flight1.setPlaneId(1234L);
        flight1.setTaxiTime(Instant.now().minus(10, ChronoUnit.MINUTES).getEpochSecond());

        Flight flight2 = new Flight();
        flight2.setActive(true);
        flight2.setId(5678L);
        flight2.setAirlinerId(1234L);
        flight2.setGateId(5678L);
        flight2.setPlaneId(1234L);
        flight2.setTaxiTime(Instant.now().minus(10, ChronoUnit.MINUTES).getEpochSecond());

        ArrayList<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);

        Mockito.when(flightControllerApi.getAllFlights()).thenReturn(flights);

        AppConfigProperties.Scheduling scheduling = new AppConfigProperties.Scheduling();
        scheduling.setGracePeriodAfterGate(15);
        Mockito.when(properties.getScheduling()).thenReturn(scheduling);

        try {
            schedulerService.generateRandomFlightSchedule();
        } catch (RuntimeException e) {
            assertEquals( "No gates available.", e.getMessage(), "Should throw an exception that details that no gates are available.");
        }

        Mockito.verify(airlinerControllerApi, Mockito.times(1)).getAllAirliners();
        Mockito.verify(planeControllerApi, Mockito.times(1)).getAllPlanes();
        Mockito.verify(gateControllerApi, Mockito.times(1)).getAllGates();
        Mockito.verify(flightControllerApi, Mockito.times(1)).getAllFlights();
    }
}
