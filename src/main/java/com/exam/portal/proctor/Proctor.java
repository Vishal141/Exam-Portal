package com.exam.portal.proctor;

import com.exam.portal.entities.Image;
import com.exam.portal.entities.jsonclasses.FaceDetectionResponse;
import com.exam.portal.entities.jsonclasses.TagDetectionResponse;
import com.exam.portal.entities.jsonclasses.Tags;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Proctor {
    private static final String API_KEY = "acc_0509d1b79051d66";
    private static final String API_SECRET = "c2b18824b72e63d25249bccdafbe5d1b";

    private static Proctor proctor = null;

    private Proctor(){}

    public static Proctor getInstance(){
        if(proctor==null)
            proctor = new Proctor();
        return proctor;
    }

    //returns true if  there is no content in image which signifies cheating.
    public boolean getResult(Image image){
        String imageUploadId = uploadFile(image);
        return (faceResult(imageUploadId)&&objectResult(imageUploadId));
    }

    //return true if there is only single face in the image
    private boolean faceResult(String imageUploadId){
        try {
            String credentialsToEncode = API_KEY + ":" + API_SECRET;
            String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

            String endpoint_url = "https://api.imagga.com/v2/faces/detections";

            String url = endpoint_url + "?image_upload_id=" + imageUploadId;
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + basicAuth);
            int responseCode = connection.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonResponse = connectionInput.readLine();
            connectionInput.close();

            System.out.println(jsonResponse);
            Gson gson = new Gson();
            FaceDetectionResponse response = gson.fromJson(jsonResponse,FaceDetectionResponse.class);

            //checking number of faces.
            return response.getResult().getFaces().size()==1;

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    //check all the tags of image and there is tag like mobile,laptop then there may be chance of cheating.
    private boolean objectResult(String imageUploadId){
        try {
            String credentialsToEncode = API_KEY + ":" + API_SECRET;
            String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

            String endpoint_url = "https://api.imagga.com/v2/tags";
            String image_url = "https://imagga.com/static/images/tagging/wind-farm-538576_640.jpg";

            String url = endpoint_url + "?image_upload_id=" + imageUploadId;
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

            connection.setRequestProperty("Authorization", "Basic " + basicAuth);

            int responseCode = connection.getResponseCode();

            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonResponse = connectionInput.readLine();
            connectionInput.close();

            System.out.println(jsonResponse);
            Gson gson = new Gson();
            TagDetectionResponse response = gson.fromJson(jsonResponse, TagDetectionResponse.class);

            //checking tags.
            for(Tags tags:response.getResult().getTags()){
                String tag = tags.getTag().getEn().toLowerCase();
                if((tag.equals("mobile") || tag.equals("laptop")) && tags.getConfidence()>=15)
                    return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    //upload the image on the api server and return an unique image which expire in 5 seconds.
    private String uploadFile(Image image){
        try{
            String credentialsToEncode = API_KEY + ":" +API_SECRET;
            String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

            String endpoint = "/tags";

            String crlf = "\r\n";
            String twoHyphens = "--";
            String boundary =  "Image Upload";

            URL urlObject = new URL("https://api.imagga.com/v2" + endpoint);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + basicAuth);
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes(twoHyphens + boundary + crlf);
            request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + image.getStudentId() + "\"" + crlf);
            request.writeBytes(crlf);

            request.write(image.decodeBytes());
            request.writeBytes(crlf);
            request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            request.flush();
            request.close();

            InputStream responseStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            String response = stringBuilder.toString();
            System.out.println(response);

            responseStream.close();
            connection.disconnect();

            return response;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
