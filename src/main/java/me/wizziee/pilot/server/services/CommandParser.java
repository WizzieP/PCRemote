package me.wizziee.pilot.server.services;

import me.wizziee.pilot.common.Command;
import me.wizziee.pilot.server.ui.UserInterface;
import me.wizziee.pilot.server.Util;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Service
public class CommandParser {

    private final CommandExecutor executor;
    private Path commandsDirPath;
    private final UserInterface ui;

    @Inject
    public CommandParser(UserInterface ui, CommandExecutor executor) {
        this.ui = ui;
        this.executor = executor;
        commandsDirPath = resolveCommandsDirPath();
        detectCommands();
    }

    private Path resolveCommandsDirPath(){
        Path parentDirPath = null;
        try {
            URI uri = CommandParser.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            Map<String, String> env = new HashMap<>();
            FileSystem fileSystem = FileSystems.newFileSystem(uri, env);
            parentDirPath = fileSystem.getPath("");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return Paths.get(parentDirPath.toString(), "commands");
    }

    public void detectCommands() {
        if(!Files.isDirectory(commandsDirPath)) {
            try {
                ui.commandsDirectoryNotFound();
                Files.createDirectory(commandsDirPath);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.walk(commandsDirPath)
                    .filter(Files::isRegularFile)
                    .forEach((Path filePath) -> {
                        Command command = parseCommand(filePath);
                        if(command != null)
                            executor.addCommand(command);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Command parseCommand(Path filePath) {
        String path = filePath.toString();
        JarFile jar = null;
        URL[] urls = null;
        try {
            jar = new JarFile(path);
            urls = new URL[]{ new URL("jar:file:" + path + "!/")};
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(jar == null || urls == null)
            return null;

        URLClassLoader classLoader = new URLClassLoader(urls);

        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String name = entry.getName().replace('/', '.');
            if(name.endsWith("Command.class")){
                try(InputStream commandInfoStream = classLoader.getResourceAsStream("info.json")) {
                    String description = Util.convertStreamToString(commandInfoStream);
                    String executorClassName = name.substring(0,entry.getName().length()-6); // -6 because of .class
                    Class<?> clazz = classLoader.loadClass(executorClassName);
                    Constructor<?> constructor = clazz.getConstructor(String.class);
                    return (Command)constructor.newInstance(description);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | NoSuchMethodException | InvocationTargetException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
