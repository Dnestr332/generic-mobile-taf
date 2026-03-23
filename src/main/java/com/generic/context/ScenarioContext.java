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

    public <V> void set(Context key, V value) {
        context.put(key, value);
    }

    public Object get(Context key) {
        return context.get(key);
    }

    public boolean contains(Context key) {
        return context.containsKey(key);
    }

    public void remove(Context key) {
        context.remove(key);
    }

    public void clear() {
        context.clear();
    }
}
