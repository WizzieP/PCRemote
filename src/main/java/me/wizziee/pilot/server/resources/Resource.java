package me.wizziee.pilot.server.resources;

import me.wizziee.pilot.common.Command;
import me.wizziee.pilot.common.CommandException;
import me.wizziee.pilot.server.CommandData;
import me.wizziee.pilot.server.services.CommandExecutor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("commands")
public class Resource {

    private final CommandExecutor commandExecutor;

    @Inject
    public Resource(CommandExecutor commandExecutor){
        this.commandExecutor = commandExecutor;
    }


    @GET
    @Produces("application/json")
    public List<String> getAllCommands(){
        return commandExecutor.getCommandsMap().values().stream()
                .map(Command::getDescription)
                .collect(Collectors.toList());
    }

    @PUT
    @Consumes("application/json")
    @Path("/execute")
    public Response executeCommand(CommandData data){
        try {
            commandExecutor.execute(data);
            return Response.ok().build();
        } catch (CommandException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
