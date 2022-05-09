package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;

import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class MainActivity2 extends AppCompatActivity {

    Button b1;
    String name, users , pass;
    EditText username  , password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        username= (EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        b1 = (Button) findViewById(R.id.login);
        name="";
        users = "";

            b1.setOnClickListener(new View.OnClickListener() {
                Intent i= new Intent();         //default
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {
                    try {
                        if (username != null && password != null) {
                            name = username.getText().toString();
                            pass = password.getText().toString();
                        }
                        //checking if the user is new or not
                        if (!name.isEmpty()) {

                        if (name.equals("Alice")) {
                            i = new Intent(MainActivity2.this, MainActivity5.class);
                            i.putExtra("username", "A");
                        } else if (name.equals("Bob")) {
                            i = new Intent(MainActivity2.this, MainActivity3.class);
                            i.putExtra("username", "B");
                        }
                        users = readFromFile("users.txt");
                        if (users.length() != 0) {
                            i.putExtra("users", true);          //Already has a key
                        } else {
                            i.putExtra("users", false);         //Flag, denoting it's new

                            generateNoteOnSD("users.txt", name + "\t" + "Alice");
                        }
                    }
                        else
                            System.out.println("NO NAME ENTERD");

                    } catch (Exception ioException) {
                        ioException.printStackTrace();
                    }
                     startActivity(i);
                }
            });

    }
    private String readFromFile(String file) {
        String ret = "";

        try {
            AssetManager am= getAssets();
            InputStream inputStream = am.open(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    public void generateNoteOnSD(String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory().getPath() , "Notes");
            name = new String(sBody.getBytes("ISO-8859-1"), "UTF-8");
            String decodedName = Html.fromHtml(name).toString();

            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(decodedName);
            writer.flush();
            writer.close();
            Toast.makeText(getBaseContext(),"Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

