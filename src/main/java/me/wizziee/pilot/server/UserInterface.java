package me.wizziee.pilot.server;

import javax.swing.*;
import java.awt.*;

public class UserInterface {
    Server server;

    public UserInterface(Server server){
        this.server = server;
        initializeTray();
    }

    private void initializeTray() {
        SystemTray systemTray = SystemTray.getSystemTray();
        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem changePortMenu = new MenuItem("Change Port");
        changePortMenu.addActionListener(server::setPortNumber);
        trayPopupMenu.add(changePortMenu);

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
        trayPopupMenu.add(exitMenuItem);

        Image image = Toolkit.getDefaultToolkit().getImage("tray_icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Pilot", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        }
        catch(AWTException e){
            e.printStackTrace();
        }
    }

    void commandsDirectoryNotFound(){
        JOptionPane.showMessageDialog(null, "'commands' directory not found, creating it.");
    }
}
