package com.rafaelwaldo.taxis.central.utils;

public class TestConstants {

    public static final String TAXI_COMMAND_EXCHANGE = "taxiCommandExchange";
    public static final String TAXI_COMMAND_FANOUT_QUEUE = "taxiCommandFanout.queue";


    private TestConstants() {
        throw new IllegalStateException("Utility class");
    }

}
