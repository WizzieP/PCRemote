package me.wizziee.pilot.server;

import me.wizziee.pilot.common.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class CommandExecutor {

    private Map<String, Command> commandsMap = new LinkedHashMap<>();

    public void addCommand(Command command){
        if(command != null)
            commandsMap.put(command.getName(), command);
    }

    public void execute(CommandData data) {
        String name = data.getName();
        commandsMap.get(name).execute(data.getParams());
    }
}
