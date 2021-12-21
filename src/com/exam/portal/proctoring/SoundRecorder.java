package com.exam.portal.proctoring;

import javafx.application.Platform;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Base64;

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

        ProctoringFile audioFile = new ProctoringFile(examId,studentId,encodeFileToBase64(wavFile),"Audio");
        //function call for uploading file.
    }

    public boolean isProctorAvailable(){
        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        return AudioSystem.isLineSupported(info);
    }

    private String encodeFileToBase64(File file){
        try{
            FileInputStream fis = new FileInputStream(file);
            String encoded = Base64.getEncoder().encodeToString(fis.readAllBytes());
            return encoded;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
