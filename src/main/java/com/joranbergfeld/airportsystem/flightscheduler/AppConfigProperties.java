package com.joranbergfeld.airportsystem.flightscheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfigProperties {

    private String clientProtocol;

    public static class PlaneClient {
        private String url;
        private int port;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class GateClient {
        private String url;
        private int port;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class AirlinerClient {
        private String url;
        private int port;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class FlightClient {
        private String url;
        private int port;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class Scheduling {
        private int gracePeriodAfterGate;

        public int getGracePeriodAfterGate() {
            return gracePeriodAfterGate;
        }

        public void setGracePeriodAfterGate(int gracePeriodAfterGate) {
            this.gracePeriodAfterGate = gracePeriodAfterGate;
        }
    }

    private PlaneClient planeClient;
    private GateClient gateClient;
    private AirlinerClient airlinerClient;
    private FlightClient flightClient;

    private Scheduling scheduling;

    public PlaneClient getPlaneClient() {
        return planeClient;
    }

    public void setPlaneClient(PlaneClient planeClient) {
        this.planeClient = planeClient;
    }

    public GateClient getGateClient() {
        return gateClient;
    }

    public void setGateClient(GateClient gateClient) {
        this.gateClient = gateClient;
    }

    public AirlinerClient getAirlinerClient() {
        return airlinerClient;
    }

    public void setAirlinerClient(AirlinerClient airlinerClient) {
        this.airlinerClient = airlinerClient;
    }

    public FlightClient getFlightClient() {
        return flightClient;
    }

    public void setFlightClient(FlightClient flightClient) {
        this.flightClient = flightClient;
    }

    public String getClientProtocol() {
        return clientProtocol;
    }

    public void setClientProtocol(String clientProtocol) {
        this.clientProtocol = clientProtocol;
    }

    public Scheduling getScheduling() {
        return scheduling;
    }

    public void setScheduling(Scheduling scheduling) {
        this.scheduling = scheduling;
    }
}
