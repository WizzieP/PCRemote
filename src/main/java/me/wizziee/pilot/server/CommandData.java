package me.wizziee.pilot.server;

import java.util.HashMap;
import java.util.Map;

public final class CommandData {
    private String name;
    private Map<String, Object> params;

    public CommandData(){}

    public CommandData(String name, Map<String, Object> params){
        this.name = name;
        this.params = new HashMap<>(params);
    }

    public Map<String, Object> getParams() {
        return new HashMap<>(params);
    }

    public String getName() {
        return name;
    }
}
