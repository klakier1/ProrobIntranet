package com.klakier.proRobIntranet.Responses.Deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Time;

public class TimeDeserializer implements JsonDeserializer<Time> {
    @Override
    public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String s = json.getAsString();
        return Time.valueOf(s);
    }
}