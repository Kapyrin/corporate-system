package com.example.timetrackingservice.service.logic;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Duration;

public class MyDurationSerializer extends StdSerializer<Duration> {

    public MyDurationSerializer() {
        super(Duration.class);
    }

    @Override
    public void serialize(Duration duration, JsonGenerator gen, SerializerProvider provider) throws IOException {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        String result = (hours > 0 ? hours + " hours" : "") +
                (minutes > 0 ? (hours > 0 ? " and " : "") + minutes + " minutes" : "");

        if (result.isEmpty()) {
            result = "0 minutes";
        }

        gen.writeString(result);
    }
}
