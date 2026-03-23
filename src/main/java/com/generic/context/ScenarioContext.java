package com.generic.context;

import com.generic.enums.Context;
import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ScenarioScope
public class ScenarioContext {

    private final Map<Context, Object> context = new ConcurrentHashMap<>();

    /**
     * Sets a value in the scenario context.
     *
     * @param key   the context key
     * @param value the value to store
     * @param <V>   the type of the value
     */
    public <V> void set(Context key, V value) {
        context.put(key, value);
    }

    /**
     * Gets a value from the scenario context.
     *
     * @param key the context key
     * @return the stored value
     */
    public Object get(Context key) {
        return context.get(key);
    }

    /**
     * Checks if the scenario context contains a key.
     *
     * @param key the context key
     * @return true if contains, false otherwise
     */
    public boolean contains(Context key) {
        return context.containsKey(key);
    }

    /**
     * Removes a value from the scenario context.
     *
     * @param key the context key
     */
    public void remove(Context key) {
        context.remove(key);
    }

    /**
     * Clears the scenario context.
     */
    public void clear() {
        context.clear();
    }
}
