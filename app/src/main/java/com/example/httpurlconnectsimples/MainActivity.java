package com.example.httpurlconnectsimples;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.httpurlconnectsimples.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());


    }
}