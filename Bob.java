
package is_project;


   
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.SecretKey;


public class Bob  {
   //1- generate public and private key for Bob 
   static KeyPair keyPair = genKeyPair(128);
   static PublicKey BobpublicKey=keyPair.getPublic();


    static String RandomKey ="mykey";
    static final String IV = "AAACCCDDDYYUURRS"; // 16 byte
 // encrypt and decrypt need the same IV.
 // initialization vector of the same size as the key, 
    private static byte[] ss;
 
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, Exception {
    
        
KeyGenerator generator = KeyGenerator.getInstance("AES");
generator.init(128); // The AES key size in number of bits
SecretKey secKey = generator.generateKey(); 
        
        PrivateKey BobprivateKey = keyPair.getPrivate();
        PrivateKey AliceprivateKey = keyPair.getPrivate();
       
        System.out.println("1- generate random key : "+RandomKey);
        
        String file = readFile("File.txt");
        
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
       byte[] byteCipherText = aesCipher.doFinal(file.getBytes());


       // String EncrypteFile = AESencrypt(RandomKey,IV,file);
        PrintWriter writer = new PrintWriter("EncryptedFile.txt", "UTF-8"); // now this is the cipher text
        writer.print(byteCipherText);
        writer.close();
        System.out.println("Encrypt the file by AES ");
        System.out.print("The file : "+ file);
        System.out.println("After encrypted : "+byteCipherText);
        //String s = Base64.getEncoder().encodeToString(encryptedBytes);
       System.out.println();
       System.out.println("2- Encryption the random key by RSA ");
       System.out.println("The key : "+RandomKey);
       
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.PUBLIC_KEY, Alice.AlicepublicKey);
byte[] encryptedKey = cipher.doFinal(secKey.getEncoded()/*Seceret Key From Step 1*/);



        // byte[] EncrypteKey= RSAencrypt(RandomKey.getBytes(),Alice.AlicepublicKey);
       // String EncrypteKeyString = Base64.getEncoder().encodeToString(EncrypteKey);
        PrintWriter writer2 = new PrintWriter("EncryptedKey.txt", "UTF-8"); // now this is the cipher text
         writer2.print(encryptedKey);
         writer2.close();
       System.out.println("After encrypted : "+encryptedKey);
       

 
    }
    
       public static KeyPair genKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
   
     public static String readFile(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        } finally {
            br.close();
        }
    }
     

    public static String AESencrypt(String key, String iv, String msg) throws Exception {
 // from here 
        byte[] bytesOfKey = key.getBytes("UTF-8"); // convert string key to byte 
        //for ( int i=0 ; i<bytesOfKey.length;i++) // key=mykey = 5 letters --> 5 byte "
        //System.out.println(bytesOfKey[i]);
        
        MessageDigest md = MessageDigest.getInstance("MD5"); // MD5 name of algo
   
        /*To be able to detect if the encrypted data has been modified in transport,
        the sender can calculate a message digest from the data and send that along with the data. 
        When you receive the encrypted data and message digest you can recalculate the message digest 
        from the data and check if the message calculated digest matches the message digest received with the data.
        If the two message digests match there is a probability that the encrypted data was not modified during transport.*/
        
        
        byte[] keyBytes = md.digest(bytesOfKey); // 16 byte 
       
//      until here MessageDigest
//      JUST for ensure if the data has been modified or not 
        final byte[] ivBytes = iv.getBytes(); // 16 byte for initial vector

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES"); // i think keyBytes is the original key but after "MessageDigest" 
        
         // Instantiate the cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // on mod CBC 
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));

        final byte[] resultBytes = cipher.doFinal(msg.getBytes());
        
    
        return Base64.getMimeEncoder().encodeToString(resultBytes);
    }
    
     public static byte[] RSAencrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");// java default "RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

 
     

   
    
}
    
    
    
    
    
    
  