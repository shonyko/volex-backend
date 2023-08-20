package ro.alexk.backend.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;

public class PinValueDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.currentToken() == JsonToken.START_ARRAY) {
            var values = new ArrayList<String>();
            jsonParser.nextToken();

            while (jsonParser.hasCurrentToken() && jsonParser.currentToken() != JsonToken.END_ARRAY) {
                values.add(jsonParser.getValueAsString());
                jsonParser.nextToken();
            }
            return String.format("[%s]", String.join(",", values));
        }
        return jsonParser.getValueAsString();
    }
}
