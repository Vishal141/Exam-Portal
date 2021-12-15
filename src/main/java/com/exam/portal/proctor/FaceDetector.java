package com.exam.portal.proctor;

import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {
    private final CascadeClassifier classifier;
    public FaceDetector(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String xmlFile = "D:/exes/opencv/sources/data/haarcascades/haarcascade_frontalface_default.xml";
        classifier = new CascadeClassifier(xmlFile);
    }

    //detecting number of faces in image.
    public int detectFace(byte[] bytes){
        try{
            //creating opencv mat object from byte array.
            Mat mat = Imgcodecs.imdecode(new MatOfByte(bytes),Imgcodecs.IMREAD_UNCHANGED);
            MatOfRect rect = new MatOfRect();

            //detecting faces in images and store face details in rect array.
            classifier.detectMultiScale(mat,rect);

            return rect.toArray().length;   //size of rect array is the count of total face in image.
        }catch (Exception e){
            e.printStackTrace();
        }
        return  1;
    }

    //checking that face working or not on a sample image.
    public boolean checkProctor(){
        Mat mat = Imgcodecs.imread("C:/Users/hp/Pictures/my_image_1.jpeg");
        MatOfRect rect = new MatOfRect();
        classifier.detectMultiScale(mat,rect);
        return rect.toArray().length==1;
    }
}
