package com.exam.portal.notificatios;

import java.awt.*;

public class Notification {
    private String title;
    private String message;

    public Notification(String title,String message){
        this.title = title;
        this.message = message;
    }

    public void show(TrayIcon.MessageType type){
        try {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
            TrayIcon trayIcon = new TrayIcon(image,"Tray Demo");
            trayIcon.setImageAutoSize(true);
            tray.add(trayIcon);
            trayIcon.setToolTip("Exam Portal");
            trayIcon.displayMessage(title,message,type);
            //System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws AWTException {
        Notification notification = new Notification("Hello world","This is my first notification");
        notification.show(TrayIcon.MessageType.INFO);
    }
}
