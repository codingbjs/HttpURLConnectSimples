package com.example.httpurlconnectsimples.httpurlconnect;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.LogRecord;

public class HttpUrlConnect {

    private static HttpUrlConnect httpUrlConnect;

    Context context;

    HttpURLConnection http;
    OutputStreamWriter outStream;
    InputStreamReader inputStreamReader;

    Handler handler;

    public HttpUrlConnect(Context context) {
        this.context = context.getApplicationContext();
        handler = new Handler();
    }

    public static synchronized HttpUrlConnect getInstance(Context context){
        if(httpUrlConnect == null) {
            httpUrlConnect = new HttpUrlConnect(context);
        }
        return httpUrlConnect;
    }


    HttpUrlConnectCallBack httpUrlConnectCallBack;

    interface HttpUrlConnectCallBack {
        void connectSendOK(String sendOkMSG);
        void connectReceiveOK(String receiveOkMSG);
        void connectErr(String connectErrMSG);
    }

    public void setHttpUrlConnectCallBack(HttpUrlConnectCallBack httpUrlConnectCallBack) {
        this.httpUrlConnectCallBack = httpUrlConnectCallBack;
    }


    public void httpPostConnect (String urlString, String requestMode, String jsonData){
        Thread httpPostConnectThread = new Thread(){
            @Override
            public void run() {
                httpUrlConnection(urlString, requestMode, jsonData);
            }
        };
        httpPostConnectThread.setDaemon(true);
        httpPostConnectThread.start();
    }


    private void httpUrlConnection(String urlString, String requestMode, String jsonData) {
        try {
            URL url = new URL(urlString);
            http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod(requestMode);
            http.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            http.setRequestProperty("Context_Type", "application/x-www-form-urlencoded; Charset=UTF-8");
            outputStreamData(jsonData);

        } catch (IOException e) {
            e.printStackTrace();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpUrlConnectCallBack.connectErr(e.toString());
                }
            });
        }
    }


    private void outputStreamData (String jsonData){
        try {
            outStream = new OutputStreamWriter(http.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(jsonData);
            writer.flush();
            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpUrlConnectCallBack.connectSendOK(http.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                inputStreamData();
            }else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpUrlConnectCallBack.connectErr(http.getResponseMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
            httpUrlConnectCallBack.connectErr(e.toString());
        }
    }

    private void inputStreamData (){
        try {
            inputStreamReader = new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuilder.append(str).append("\n");
            }
            httpUrlConnectCallBack.connectReceiveOK(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
            httpUrlConnectCallBack.connectErr(e.toString());
        }
    }

}
