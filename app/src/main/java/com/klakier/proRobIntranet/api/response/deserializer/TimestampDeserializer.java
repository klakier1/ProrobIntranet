package com.klakier.proRobIntranet.api.response.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {
    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String s = json.getAsString();
        return Timestamp.valueOf(s);
    }
}
