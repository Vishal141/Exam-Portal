package com.exam.portal.proctoring;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import javafx.application.Platform;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class ScreenRecorder {
    private int recordTime;
    private String examId;
    private String studentId;

    private int width,height;

    private File file;

    private volatile boolean isThreadRunning;

    public ScreenRecorder(){
        try{
            file = File.createTempFile("screenFile",".mp4");
            //file = new File("E:\\screen.mp4");
        }catch (Exception e){
            e.printStackTrace();
        }
        setWidthAndHeight();
    }

    public ScreenRecorder(int recordTime, String examId, String studentId) {
        this();
        this.recordTime = recordTime;
        this.examId = examId;
        this.studentId = studentId;
    }

    public void setRecordTime(int recordTime) {
        this.recordTime = recordTime;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    private void setWidthAndHeight(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.width = (int) dimension.getWidth();
        this.height = (int) dimension.getHeight();
        this.width = (int) (Math.ceil(width/2)*2);
        this.height = (int) (Math.ceil(height/2))*2;
    }

    private void start(){
        isThreadRunning = true;
        new Thread(()->{
            IMediaWriter writer = ToolFactory.makeWriter(file.getAbsolutePath());
            writer.addVideoStream(0,0, ICodec.ID.CODEC_ID_H264,width,height);
            long startTime = System.nanoTime();
            while (true)
            {
                try{
                    Robot robot = new Robot();
                    BufferedImage image = robot.createScreenCapture(new Rectangle(width,height));
                    BufferedImage capImage = image;
                    if(image.getType() != BufferedImage.TYPE_3BYTE_BGR){
                        capImage = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
                        capImage.getGraphics().drawImage(image,0,0,null);
                    }
                    writer.encodeVideo(0,capImage,System.nanoTime()-startTime, TimeUnit.NANOSECONDS);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(!isThreadRunning){
                    break;
                }
            }

            writer.flush();
            writer.close();
            System.out.println("finished");
            finish();
        }).start();
    }

    public void finish(){
        ProctoringFile screenFile = new ProctoringFile(examId,studentId,encodeFileToBase64(file),"Screen");
        //function call for uploading file.
    }

    public void startRecording(){
        Platform.runLater(()->{
            try{
                Thread.sleep(this.recordTime);
            }catch (Exception e){
                e.printStackTrace();
            }

            this.isThreadRunning = false;
        });

        this.start();
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
