package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity7 extends AppCompatActivity {
    public TextView t6;
    public TextView t7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        t6=(TextView)findViewById(R.id.textView7);
        t7=(TextView)findViewById(R.id.textView7);

    }
}