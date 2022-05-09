package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
public class MainActivity3 extends AppCompatActivity {
    static SecretKey secKey;
    static String stringKey;

    static KeyPairGenerator kpg;
    static KeyPair keyPair;

    static PublicKey BobpublicKey;
    static PublicKey AlicepublicKey;
    static PrivateKey BobprivateKey;
    static PrivateKey AliceprivateKey;
    static byte[] EncryptedFile;
    Button b1;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //Listening from sent intent
        Intent in2 = this.getIntent();
        boolean signed = in2.getBooleanExtra("users", false);
        String name = in2.getStringExtra("username");
            init();
        b1=(Button)findViewById(R.id.button);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText message = (EditText) findViewById(R.id.editTextTextPersonName3);
                if(message != null) {
                    String text = message.getText().toString();
                    try {
                        byte[] EncryptedKey = Enc(text);
                        Dec(EncryptedKey);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i= new Intent(MainActivity3.this , MainActivity4.class);
                    startActivity(i);
                }
            }
        });
    }

    public void init() {
        try {
            this.secKey = KeyGenerator.getInstance("AES").generateKey(); // random key
            this.stringKey = secKey.toString();
            this.kpg = KeyPairGenerator.getInstance("RSA"); // public/private keys
            this.kpg.initialize(2048);
            this.keyPair = kpg.generateKeyPair();
            this.BobpublicKey = keyPair.getPublic();
            this.AlicepublicKey = keyPair.getPublic();
            this.BobprivateKey = keyPair.getPrivate();
            this.AliceprivateKey = keyPair.getPrivate();

            //writing
            String name= AlicepublicKey+"\n"+AliceprivateKey;
            writefile("users" , name);
        }
        catch (NoSuchAlgorithmException | FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] Enc(String file) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        byte[] encryptedKey = new byte[0];
        try {
            System.out.println("--------------------- BOB -------------------");
            stringKey = secKey.toString().substring(stringKey.lastIndexOf(".") + 1);  //TO AVOID UNNECESSARY CHARACTERS
            System.out.println("1- generate random key : " + stringKey);
            System.out.println("Encrypt the file by AES ");
            EncryptedFile = AES_Encryption(file, secKey);
            writefile("EncryptedFile.txt" ,EncryptedFile.toString());
            System.out.println("The file : " + file);
            System.out.println("After encrypted : " + EncryptedFile);
            this.EncryptedFile= EncryptedFile;
            ////////////////////////////////////////////////////END OF ENC FILE////////////////////////////
            System.out.println();
            System.out.println("2- Encryption the random key by RSA ");
            System.out.println("The key : " + stringKey);
            encryptedKey = RSA_Encryption(AlicepublicKey, secKey);
            writefile("EncryptedKey.txt", encryptedKey.toString()); // now this is the cipher text
            System.out.println("After encrypted : " + encryptedKey);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encryptedKey;
    }
    public void Dec(byte[] encryptedKey)throws Exception , NoSuchAlgorithmException{
        System.out.println("--------------------- ALICE -------------------");
        System.out.println("3- Decryption the random key by RSA ");
        String EncryptedKeyFromFile=encryptedKey.toString();
        System.out.print("The encrypted key : "+EncryptedKeyFromFile);
        byte[] decryptedKey = RSA_Decryption(AliceprivateKey,encryptedKey);
        System.out.println("After decrypted : "+decryptedKey);


        System.out.println();
        System.out.println("4- Decryption the File by AES ");
        String plainText= AES_Decryption(EncryptedFile,decryptedKey);
        String cipher=EncryptedFile.toString();
        System.out.print("The enrypted file : "+cipher);
        System.out.println("After decrypted : "+plainText);
        writefile("Plaintext.txt",plainText);
    }
    //////////////////////////////////////-HELPER METHODS-//////////////////////////////////

    public static KeyPair genKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String AES_Decryption(byte[] EncryptedFile, byte[] decryptedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKey originalKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(EncryptedFile);
        String plainText = new String(bytePlainText);
        return plainText;
    }

    public static byte[] RSA_Decryption(PrivateKey AliceprivateKey, byte[] encryptedKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, AliceprivateKey);
        byte[] decryptedKey = cipher.doFinal(encryptedKey);
        return decryptedKey;
    }

    private byte[] RSA_Encryption(PublicKey alicepublicKey, SecretKey secKey) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        cipher.init(Cipher.PUBLIC_KEY, AlicepublicKey);
        byte[] encryptedKey = cipher.doFinal(secKey.getEncoded()/*Seceret Key From Step 1*/);
        return encryptedKey;
    }

    private byte[] AES_Encryption(String file, SecretKey secKey) throws BadPaddingException, IllegalBlockSizeException {
        Cipher aesCipher = null;
        try {
            aesCipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] byteCipherText = aesCipher.doFinal(file.getBytes());
        return byteCipherText;
    }
    private String readFromFile(String file) {
        FileReader fr = null;
        String fileContents="";
        File myExternalFile = new File(getExternalFilesDir(file), file);
        StringBuilder stringBuilder = new StringBuilder();
        try {

            fr = new FileReader(myExternalFile);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null){
                stringBuilder.append(line).append('\n');
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            fileContents = "File contents\n" + stringBuilder.toString();
            // Set the TextView with fileContents
        }
        return fileContents;
    }

    public void writefile(String name , String  content) throws IOException {
            try {
                File root = new File(Environment.getExternalStorageDirectory() , "Notes");
                name = new String(content.getBytes("windows-1252"), "windows-1252");
                String decodedName = Html.fromHtml(name).toString();

                if (!root.exists()) {
                    root.mkdirs();
                }

                File gpxfile = new File(root, name);
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