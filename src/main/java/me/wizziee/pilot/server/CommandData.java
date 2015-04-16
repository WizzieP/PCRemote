package me.wizziee.pilot.server;

import java.util.Map;

public class CommandData {
    private String name;
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
