package com.exam.portal.proctoring;

import java.io.IOException;
import java.util.Scanner;

public class ProcessesDetails {

    public int getProcessesCount(){
        try {
            Process process = new ProcessBuilder("powershell","\"gps| ? {$_.mainwindowtitle.length -ne 0} | Format-Table -HideTableHeaders name, ID").start();
            Scanner sc = new Scanner(process.getInputStream());
            int count = 0;
            if(sc.hasNextLine())sc.nextLine();
            while (sc.hasNextLine()){
                count++;
                sc.nextLine();
            }
            return count-4;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


