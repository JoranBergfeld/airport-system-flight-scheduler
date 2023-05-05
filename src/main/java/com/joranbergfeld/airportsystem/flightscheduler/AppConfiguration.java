package com.joranbergfeld.airportsystem.flightscheduler;

import com.joranbergfeld.airport_system.airliner.client.api.AirlinerControllerApi;
import com.joranbergfeld.airport_system.flight.client.api.FlightControllerApi;
import com.joranbergfeld.airport_system.gate.client.api.GateControllerApi;
import com.joranbergfeld.airport_system.plane.client.api.PlaneControllerApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    private final AppConfigProperties appConfigProperties;

    public AppConfiguration(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    @Bean
    AirlinerControllerApi airlinerControllerApi() {
        com.joranbergfeld.airport_system.airliner.client.invoker.ApiClient client = new com.joranbergfeld.airport_system.airliner.client.invoker.ApiClient();
        client.setBasePath(appConfigProperties.getClientProtocol() + "://" + appConfigProperties.getAirlinerClient().getUrl() + ":" + appConfigProperties.getAirlinerClient().getPort());
        return new AirlinerControllerApi(client);
    }

    @Bean
    GateControllerApi gateControllerApi() {
        com.joranbergfeld.airport_system.gate.client.invoker.ApiClient client = new com.joranbergfeld.airport_system.gate.client.invoker.ApiClient();
        client.setBasePath(appConfigProperties.getClientProtocol() + "://" + appConfigProperties.getGateClient().getUrl() + ":" + appConfigProperties.getGateClient().getPort());
        return new GateControllerApi(client);
    }

    @Bean
    PlaneControllerApi planeControllerApi() {
        com.joranbergfeld.airport_system.plane.client.invoker.ApiClient client = new com.joranbergfeld.airport_system.plane.client.invoker.ApiClient();
        client.setBasePath(appConfigProperties.getClientProtocol() + "://" + appConfigProperties.getPlaneClient().getUrl() + ":" + appConfigProperties.getPlaneClient().getPort());
        return new PlaneControllerApi(client);
    }

    @Bean
    FlightControllerApi flightControllerApi() {
        com.joranbergfeld.airport_system.flight.client.invoker.ApiClient client = new com.joranbergfeld.airport_system.flight.client.invoker.ApiClient();
        client.setBasePath(appConfigProperties.getClientProtocol() + "://" + appConfigProperties.getFlightClient().getUrl() + ":" + appConfigProperties.getFlightClient().getPort());
        return new FlightControllerApi(client);
    }
}
