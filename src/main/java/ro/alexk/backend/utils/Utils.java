package ro.alexk.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import ro.alexk.backend.utils.errors.Error;
import ro.alexk.backend.utils.option.None;
import ro.alexk.backend.utils.option.Some;
import ro.alexk.backend.utils.result.Err;
import ro.alexk.backend.utils.result.Ok;
import ro.alexk.backend.utils.result.Result;

import java.util.function.Consumer;

public class Utils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Result<T> deserialize(Class<T> type, Object... objects) {
        var json = objects[0].toString();
        try {
            return ok(objectMapper.readValue(json, type));
        } catch (JsonProcessingException e) {
            return err(e);
        }
    }

    public static <T> Result<String> toJson(T obj) {
        try {
            return ok(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            return err(e);
        }
    }

    public static <T> Result<JSONObject> toJsonObject(T obj) {
        try {
            var json = objectMapper.writeValueAsString(obj);
            return ok(new JSONObject(json));
        } catch (Exception e) {
            return err(e);
        }
    }

    public static <T> Some<T> some(T val) {
        return new Some<>(val);
    }

    public static <T> None<T> none() {
        return new None<>();
    }

    public static <V> Ok<V> ok(V val) {
        return new Ok<>(val);
    }

    public static <V> Err<V> err(String msg) {
        return new Err<>(new Error(msg));
    }

    public static <V> Err<V> err(String msg, Throwable cause) {
        return new Err<>(new Error(msg, cause));
    }

    public static <V> Err<V> err(Throwable cause) {
        return new Err<>(new Error(cause));
    }

    public static <V> void okOrElse(Result<V> res, Consumer<V> ok, Consumer<Error> el) {
        if(res instanceof Ok<V> o) {
            ok.accept(o.value());
        } else if (res instanceof Err<V> e) {
            el.accept(e.err());
        }
    }
}
