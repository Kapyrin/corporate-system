package com.example.timetrackingservice.service.logic;

import com.example.timetrackingservice.service.logic.enity.DateRange;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DateRangeSerializer extends StdSerializer<DateRange> {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DateRangeSerializer() {
        super(DateRange.class);
    }

    @Override
    public void serialize(DateRange range, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("from", range.from().toLocalTime().format(TIME_FORMATTER));
        gen.writeStringField("to", range.to().toLocalTime().format(TIME_FORMATTER));
        gen.writeEndObject();
    }
}
