package ro.alexk.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONObject;

public class Utils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T deserialize(Class<T> type, Object... objects) throws JsonProcessingException {
        var json = objects[0].toString();
        return objectMapper.readValue(json, type);
    }

    @SneakyThrows
    public static <T> String toJson(T obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> JSONObject toJsonObject(T obj) {
        var json = objectMapper.writeValueAsString(obj);
        return new JSONObject(json);
    }
}
