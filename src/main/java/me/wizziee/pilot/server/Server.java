package me.wizziee.pilot.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.wizziee.pilot.common.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Server {

    private int portNumber = 17;

    UserInterface ui;
    CommandParser commandParser;
    CommandExecutor commandExecutor;

    public Server() {
        ui = new UserInterface(this);
        commandExecutor = new CommandExecutor();
        commandParser = new CommandParser(ui, commandExecutor);
        connect(portNumber);
    }


    void setPortNumber(ActionEvent e){
        portNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "Port number:", portNumber));
        connect(portNumber);
    }


    private void connect(int portNumber){
        try(
                ServerSocket server = new ServerSocket(portNumber);
                Socket socket = server.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ){
            ObjectMapper mapper = new ObjectMapper();
            CommandData data = mapper.readValue(reader, CommandData.class);
            commandExecutor.execute(data);

        } catch(IOException e) {
            System.out.println("Exception while listening to port: " + portNumber);
            System.out.println(e.getMessage());
        }
    }
}
