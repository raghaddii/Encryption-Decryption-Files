
package is_project;


import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Bob_Alic  {

 public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, Exception {
    
  
SecretKey secKey = KeyGenerator.getInstance("AES").generateKey(); // random key
String stringKey=secKey.toString();

KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); // public/private keys
kpg.initialize(2048);
KeyPair keyPair = kpg.generateKeyPair();

PublicKey BobpublicKey = keyPair.getPublic();
PublicKey AlicepublicKey = keyPair.getPublic();

PrivateKey BobprivateKey = keyPair.getPrivate(); 
PrivateKey AliceprivateKey = keyPair.getPrivate(); 
 
      
        
        System.out.println("--------------------- BOB -------------------");
        System.out.println("1- generate random key : "+stringKey);
        System.out.println("Encrypt the file by AES ");
        String file = readFile("File.txt");
        byte[] EncryptedFile = AES_Encryption(file,secKey);
        PrintWriter writer = new PrintWriter("EncryptedFile.txt", "UTF-8"); // now this is the cipher text
        writer.print(EncryptedFile);
        writer.close();
        System.out.print("The file : "+ file);
        System.out.println("After encrypted : "+EncryptedFile);
      
        
       System.out.println();
       System.out.println("2- Encryption the random key by RSA ");
       System.out.println("The key : "+stringKey);
       byte[] encryptedKey=RSA_Encryption(AlicepublicKey,secKey);
       PrintWriter writer2 = new PrintWriter("EncryptedKey.txt", "UTF-8"); // now this is the cipher text
       writer2.print(encryptedKey);
       writer2.close();
       System.out.println("After encrypted : "+encryptedKey);

       System.out.println();
       System.out.println("--------------------- ALICE -------------------");
       System.out.println("3- Decryption the random key by RSA ");
       String EncryptedKeyFromFile=readFile("EncryptedKey.txt");
       System.out.print("The key : "+EncryptedKeyFromFile);
       byte[] decryptedKey = RSA_Decryption(AliceprivateKey,encryptedKey);
       System.out.println("After decrypted : "+decryptedKey);

 
       System.out.println();
       System.out.println("4- Decryption the File by AES ");
       String plainText= AES_Decryption(EncryptedFile,decryptedKey);
       String cipher=readFile("EncryptedFile.txt");
       System.out.print("The file : "+cipher);
       System.out.println("After decrypted : "+plainText);

  
 
    }
    
       public static KeyPair genKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
 
   
     public static String readFile(String filename) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            return everything;
        }
    }
     
public static byte[] AES_Encryption(String file ,SecretKey secKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(file.getBytes());   
        return byteCipherText;   
}
public static String AES_Decryption(byte[] EncryptedFile ,byte[] decryptedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey .length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(EncryptedFile);
        String plainText = new String(bytePlainText); 
        return plainText;   
}

  public static byte[] RSA_Encryption(PublicKey AlicepublicKey ,SecretKey secKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PUBLIC_KEY, AlicepublicKey);
        byte[] encryptedKey = cipher.doFinal(secKey.getEncoded()/*Seceret Key From Step 1*/);
        return encryptedKey;
  } 
  
public static byte[] RSA_Decryption(PrivateKey AliceprivateKey ,byte [] encryptedKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
     Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
     cipher.init(Cipher.PRIVATE_KEY, AliceprivateKey);
     byte[] decryptedKey = cipher.doFinal(encryptedKey);
     return decryptedKey;
}

    
}
    
    
    
    
    
    
  