package com.example.httpurlconnectsimples;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.httpurlconnectsimples.databinding.ActivityMainBinding;
import com.example.httpurlconnectsimples.httpurlconnect.HttpUrlConnect;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ActivityMainBinding mainBinding;

    HttpUrlConnect httpUrlConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        httpUrlConnect = HttpUrlConnect.getInstance(getApplicationContext());

        httpUrlConnect.setHttpUrlConnectCallBack(new HttpUrlConnect.HttpUrlConnectCallBack() {
            @Override
            public void connectSendOK(String sendOkMSG) {
                mainBinding.getHttpTextView.setText(sendOkMSG);
                mainBinding.getHttpTextView.append("\n\n");

            }

            @Override
            public void connectReceiveOK(String receiveOkMSG) {
                mainBinding.getHttpTextView.append(receiveOkMSG);
                mainBinding.getHttpTextView.append("\n");
            }

            @Override
            public void connectErr(String connectErrMSG) {
                Log.e(TAG, connectErrMSG);
            }
        });

        mainBinding.httpConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpUrlConnect.httpPostConnect(
                        mainBinding.inputURLEditText.getText().toString(),
                        "POST", mainBinding.inputJsonEditText.getText().toString());
            }
        });

        mainBinding.inputURLEditText.setText("http://www.mfsoft.kr:8088/appMeter/tollGate");
        mainBinding.inputJsonEditText.setText("{\"routeNo\":\"100\"}");

    }
}