package me.wizziee.pilot.server.services;

import me.wizziee.pilot.common.Command;
import me.wizziee.pilot.common.CommandException;
import me.wizziee.pilot.server.CommandData;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CommandExecutor {

    private Map<String, Command> commandsMap = new LinkedHashMap<>();

    public Map<String, Command> getCommandsMap() {
        return commandsMap;
    }

    public void addCommand(Command command){
        if(command != null && !commandsMap.containsValue(command))
            commandsMap.put(command.getName(), command);
    }

    public void execute(CommandData data) throws CommandException {
        String name = data.getName();
        commandsMap.get(name).execute(data.getParams());
    }
}
