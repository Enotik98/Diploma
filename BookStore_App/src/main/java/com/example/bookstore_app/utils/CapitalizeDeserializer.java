package com.example.bookstore_app.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CapitalizeDeserializer extends StdDeserializer<String> {
    public CapitalizeDeserializer(){
        this(null);
    }
    public CapitalizeDeserializer(Class<?>  vc){
        super(vc);
    }
    @Override
    public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        String value = node.asText();
        return value.substring(0, 1).toUpperCase() + value.substring(1);
    }
}
