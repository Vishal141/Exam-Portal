package com.exam.portal.proctoring;

import com.exam.portal.models.ProctoringFile;
import com.exam.portal.notificatios.Notification;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class WebcamRecorder {
    private int recordTime;
    private String examId;
    private String studentId;

    private File file;
    private ProctorResult result;
    private ProctoringFile proctoringFile;

    private volatile boolean isThreadRunning;

    public WebcamRecorder(){
        try{
            file = File.createTempFile("webcam-recording",".mp4");
            //file = new File("E:/webcam.mp4");
        }catch (Exception e){
            e.printStackTrace();
        }
        isThreadRunning = false;
        result = new ProctorResult();
        proctoringFile = new ProctoringFile();
    }

    public WebcamRecorder(int recordTime, String examId, String studentId) {
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

    public boolean isWebcamAvailable(){
        Webcam webcam = Webcam.getDefault();
        return webcam == null;
    }

    private void start(){
        isThreadRunning = true;
        Thread thread = new Thread(()->{
            IMediaWriter writer = ToolFactory.makeWriter(file.getAbsolutePath());
            Dimension dimension = WebcamResolution.QVGA.getSize();
            writer.addVideoStream(0,0, ICodec.ID.CODEC_ID_H264,dimension.width,dimension.height);

            Webcam webcam = Webcam.getDefault();
            webcam.setViewSize(dimension);
            webcam.open(true);
            System.out.println("Recording started...");
            long start = System.currentTimeMillis();
            boolean keyFrame = true;
            while (isThreadRunning){
                BufferedImage image = ConverterFactory.convertToType(webcam.getImage(),BufferedImage.TYPE_3BYTE_BGR);
                IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);
                long time = (System.currentTimeMillis()-start)*1000;
                IVideoPicture frame = converter.toPicture(image,time);
                if(keyFrame){
                    frame.setKeyFrame(true);
                    keyFrame = false;
                }
                frame.setQuality(0);
                writer.encodeVideo(0,frame);

                new Thread(()->{
                    try{
                        long t = time;
                        Server server = ServerHandler.getInstance();
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(image,"jpg",os);
                        String bytes = Base64.getEncoder().encodeToString(os.toByteArray());
                        int faces = server.detectFace(bytes);
                        if(faces != 1){
                            result.addTime(t);
                            result.setCheatStatus(recordTime);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            writer.flush();
            writer.close();
            webcam.close();
        });

        thread.start();
    }

    public void finish(){
        isThreadRunning = false;
        System.out.println("finished");
        proctoringFile.setExamId(this.examId);
        proctoringFile.setType("Webcam-Video");
        proctoringFile.setStudentId(this.studentId);
        proctoringFile.setFile(encodeFileToBase64(file));
        proctoringFile.setProctorResult(result);

        if(result.getCheatStatus()){
            //Give Notification
            Notification notification = new Notification("Warning","It has been detected that you cheat in the exam.");
            notification.show(TrayIcon.MessageType.WARNING);
            //send proctoring file to server for reference.
            proctoringFile.setResult(result);
            Server server = ServerHandler.getInstance();
            server.sendProctorFile(proctoringFile);
        }
    }

    public void startRecording(){
        new Thread(()->{
            try {
                Thread.sleep(this.recordTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }).start();

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

    public static void main(String[] args) {
        WebcamRecorder recorder = new WebcamRecorder();
        recorder.setRecordTime(60000);
        recorder.setStudentId("S54d51sd");
        recorder.setExamId("Exam#54e7d0");

        recorder.startRecording();
    }
}
