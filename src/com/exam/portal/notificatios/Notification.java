package com.exam.portal.notificatios;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Notification {
    private final String title;
    private final String message;

    public Notification(String title,String message){
        this.title = title;
        this.message = message;
    }

    //this function shows the notification with the help of system tray.
    public void show(TrayIcon.MessageType type){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image,"Tray Demo");
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            trayIcon.setToolTip("Exam Portal");
            trayIcon.displayMessage(title,message,type);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String path = File.listRoots()[1].getAbsolutePath()+"exam_portal\\info.ep";
        Path path1 = Paths.get(path);
        try {
            Files.createFile(path1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
