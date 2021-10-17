package com.exam.portal.proctor;

import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class FaceDetector {
    private final CascadeClassifier classifier;
    public FaceDetector(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String xmlFile = "D:/exes/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml";
        classifier = new CascadeClassifier(xmlFile);
    }

    public int detectFace(byte[] bytes){
        try{
            Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes),Imgcodecs.IMREAD_UNCHANGED);
            MatOfRect rect = new MatOfRect();

            classifier.detectMultiScale(mat,rect);

            return rect.toArray().length;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  1;
    }

    public boolean checkProctor(){
        Mat mat = Imgcodecs.imread("C:/Users/hp/Pictures/my_image_1.jpeg");
        MatOfRect rect = new MatOfRect();
        classifier.detectMultiScale(mat,rect);
        return rect.toArray().length==1;
    }
}
