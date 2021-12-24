package com.exam.portal.proctoring;

import com.exam.portal.models.Image;
import com.exam.portal.server.Server;
import com.exam.portal.server.ServerHandler;
import com.exam.portal.student.StudentController;
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
import java.util.Base64;

public class WebcamRecorder {
    private long recordTime;
    private File file;

    private volatile int totalCheck;   //total images sends to server for checking.
    private volatile int cheatFound;   //total times server false in not cheat status.
    private volatile boolean isThreadRunning;

    public WebcamRecorder(){
        try{
            file = File.createTempFile("webcam-recording",".mp4");
            file.deleteOnExit();
            //file = new File("E:/webcam.mp4");
        }catch (Exception e){
            e.printStackTrace();
        }
        isThreadRunning = false;
        totalCheck = 0;
        cheatFound = 0;
    }

    public WebcamRecorder(int recordTime) {
        this();
        this.recordTime = (long) recordTime *60*1000;
    }

    public void setRecordTime(long recordTime) {
        this.recordTime = recordTime;
    }

    //returns true if webcam is working otherwise return false.
    public boolean isWebcamAvailable(){
        Webcam webcam = Webcam.getDefault();
        return webcam != null;
    }

    //start recording student's video and sending image to server for proctoring.
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

                new Thread(()->{             //in different thread image send to server and checked for cheating.
                    try{
                        long t = time;
                        Server server = ServerHandler.getInstance();
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(image,"jpg",os);
                        String bytes = Base64.getEncoder().encodeToString(os.toByteArray());
                        Image image1 = new Image();
                        image1.setStudentId(StudentController.student.getStudentId());
                        image1.setBytes(bytes);
                        if(!server.getProctorResult(image1)){
                            cheatFound++;
                        }
                        totalCheck++;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();

                try {
                    Thread.sleep(5000);
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
    }

    public boolean getCheatStatus(){
       int percentage = (int) Math.ceil(cheatFound*100.00/totalCheck);
       return percentage>=5;
    }

    public void startRecording(){
        new Thread(()->{
            try {
                Thread.sleep(recordTime);      //sleeping this thread for recordTime milliseconds after that stopping recording.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }).start();

        this.start();
    }
}
