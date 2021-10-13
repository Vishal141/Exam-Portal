package com.exam.portal.proctoring;

import com.exam.portal.models.ProctoringFile;
import javafx.application.Platform;

import javax.sound.sampled.*;
import java.io.*;

public class SoundRecorder {
    private int recordTime;
    private String examId;
    private String studentId;

    private File wavFile;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    TargetDataLine line;

    public SoundRecorder(){
        try {
            wavFile = File.createTempFile("audioFile",".wav");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public SoundRecorder(int recordTime,String examId,String studentId){
        this();
        this.recordTime = recordTime;
        this.examId = examId;
        this.studentId = studentId;
    }

    public void setRecordTime(int recordTime){
        this.recordTime = recordTime;
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        return format;
    }

    void start() {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            // start recording
            AudioSystem.write(ais, fileType, wavFile);

        } catch (LineUnavailableException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void startRecording(){
        Platform.runLater(()->{
            try{
                Thread.sleep(recordTime);
            }catch (Exception e){
                e.printStackTrace();
            }

            finish();
        });

        start();
    }

    /**
     * Closes the target data line to finish capturing and recording
     */
    void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");

        ProctoringFile audioFile = new ProctoringFile(examId,studentId,wavFile,"Audio");
        //function call for uploading file.
    }

    public boolean isProctorAvailable(){
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        // checks if system supports the data line
        //System.exit(0);
        return AudioSystem.isLineSupported(info);
    }

//    public static void main(String[] args) {
//        final SoundRecorder recorder = new SoundRecorder();
//
//        // creates a new thread that waits for a specified
//        // of time before stopping
//        Thread stopper = new Thread(new Runnable() {
//            public void run() {
//                try {
//                    Thread.sleep(recordTime);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                recorder.finish();
//            }
//        });
//
//        stopper.start();
//
//        // start recording
//        recorder.start();
//    }
}
