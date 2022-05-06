
package is_nw;



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

public class Bob {
    
    static SecretKey secKey ;
    static String stringKey;

    static KeyPairGenerator kpg ;
    static KeyPair keyPair;

    static PublicKey BobpublicKey ;
    static PublicKey AlicepublicKey ;
    static PrivateKey BobprivateKey ;
    static PrivateKey AliceprivateKey ;
    static byte[] EncryptedFile;
    
        public Bob() throws NoSuchAlgorithmException
    {
        secKey = KeyGenerator.getInstance("AES").generateKey(); // random key
        stringKey=secKey.toString();
        kpg = KeyPairGenerator.getInstance("RSA"); // public/private keys
        kpg.initialize(2048);
        keyPair = kpg.generateKeyPair();
        BobpublicKey = keyPair.getPublic();
        AlicepublicKey = keyPair.getPublic();
        BobprivateKey = keyPair.getPrivate(); 
        AliceprivateKey = keyPair.getPrivate(); 
    }
            static class ENCRYPTION{
        public byte[]  Enc()throws Exception , NoSuchAlgorithmException{
            System.out.println("--------------------- BOB -------------------");
            stringKey=secKey.toString().substring(stringKey.lastIndexOf(".") + 1);  //TO AVOID UNNECESSARY CHARACTERS
            System.out.println("1- generate random key : "+stringKey);
            System.out.println("Encrypt the file by AES ");
            String file = readFile("File.txt");//TOBE CHANGED BASED ON THE DIRECTORY
            EncryptedFile = AES_Encryption(file,secKey);
            PrintWriter writer = new PrintWriter("EncryptedFile.txt", "UTF-8"); // now this is the cipher text
            writer.print(EncryptedFile);
            writer.close();
            System.out.print("The file : "+ file);
            System.out.println("After encrypted : "+EncryptedFile);
            ////////////////////////////////////////////////////END OF ENC FILE////////////////////////////
            System.out.println();
            System.out.println("2- Encryption the random key by RSA ");
            System.out.println("The key : "+stringKey);
            byte[] encryptedKey=RSA_Encryption(AlicepublicKey,secKey);
            PrintWriter writer2 = new PrintWriter("EncryptedKey.txt", "UTF-8"); // now this is the cipher text
            writer2.print(encryptedKey);
            writer2.close();
            System.out.println("After encrypted : "+encryptedKey);
            return encryptedKey;
        }
    }
            
                public static String readFile(String filename) throws Exception, NoSuchAlgorithmException {
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
                     public static byte[] RSA_Encryption(PublicKey AlicepublicKey ,SecretKey secKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.PUBLIC_KEY, AlicepublicKey);
            byte[] encryptedKey = cipher.doFinal(secKey.getEncoded()/*Seceret Key From Step 1*/);
            return encryptedKey;
      }
           public static byte[]  callEnc() throws Exception , NoSuchAlgorithmException 
    {
        ENCRYPTION n= new ENCRYPTION();
        byte[] encryptedKey =n.Enc();
        return encryptedKey;
    }           
                     
                     public static void main(String[] args) throws Exception, NoSuchAlgorithmException  {

    Bob c= new Bob();
    byte[] encryptedKey= c.callEnc();
    
    }
}
