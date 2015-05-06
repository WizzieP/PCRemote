package me.wizziee.pilot.server.config;

import me.wizziee.pilot.server.resources.Resource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(){
        register(Resource.class);
    }
}
