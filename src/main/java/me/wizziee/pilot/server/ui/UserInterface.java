package me.wizziee.pilot.server.ui;

import me.wizziee.pilot.server.services.CommandParser;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

@Service
public class UserInterface {

    private final SystemTray systemTray;
    private PopupMenu trayPopupMenu;

    @Inject
    public UserInterface() {
        systemTray = SystemTray.getSystemTray();
        initializeTray();
    }

    private void initializeTray() {
        trayPopupMenu = new PopupMenu();

        initializeMenu();
        setIcon();
    }

    private void setIcon() {
        Image image = Toolkit.getDefaultToolkit().getImage("tray_icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Pilot", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void initializeMenu() {
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        trayPopupMenu.add(exitMenuItem);
    }

    public void commandsDirectoryNotFound() {
        JOptionPane.showMessageDialog(null, "'commands' directory not found, creating it.");
    }
}
