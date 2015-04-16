package me.wizziee.pilot.server;

import me.wizziee.pilot.common.Command;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CommandParser {

    private CommandExecutor executor;
    private Path commandsDirPath;
    private UserInterface ui;

    public CommandParser(UserInterface ui, CommandExecutor executor) {
        this.ui = ui;
        this.executor = executor;
        commandsDirPath = resolveCommandsDirPath();
        detectCommands();
    }

    private Path resolveCommandsDirPath(){
        Path parentDirPath = null;
        try {
            parentDirPath = Paths.get(Server.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Paths.get(parentDirPath.toString(), "commands");
    }

    void detectCommands() {
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
            if(name.contains("Command.class")){
                String executorClassName = name.substring(0,entry.getName().length()-6); // -6 because of .class
                try {
                    Class clazz = classLoader.loadClass(executorClassName);
                    return (Command)clazz.newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
