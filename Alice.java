
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


public class Alice {
 
   
    static KeyPairGenerator kpg ;
    static KeyPair keyPair;

    static PublicKey AlicepublicKey ;
    static PrivateKey AliceprivateKey ;
   

    public Alice() throws NoSuchAlgorithmException
    {
      
        kpg = KeyPairGenerator.getInstance("RSA"); // public/private keys
        kpg.initialize(2048);
        keyPair = kpg.generateKeyPair();

        AlicepublicKey = keyPair.getPublic();
        AliceprivateKey = keyPair.getPrivate(); 
    }
      static class Decryption{
       public void Dec(byte[] encryptedKey)throws Exception , NoSuchAlgorithmException{
       System.out.println("--------------------- ALICE -------------------");
       System.out.println("3- Decryption the random key by RSA ");
       String EncryptedKeyFromFile=readFile("EncryptedKey.txt");
       System.out.print("The encrypted key : "+EncryptedKeyFromFile);
       byte[] decryptedKey = RSA_Decryption(AliceprivateKey,encryptedKey);
       System.out.println("After decrypted : "+decryptedKey);

 
       System.out.println();
       System.out.println("4- Decryption the File by AES ");
       String plainText= AES_Decryption(Bob.EncryptedFile,decryptedKey);
       String cipher=readFile("EncryptedFile.txt");
       System.out.print("The encrypted file : "+cipher);
       System.out.println("After decrypted : "+plainText);
            
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
           public static String AES_Decryption(byte[] EncryptedFile ,byte[] decryptedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
            SecretKey originalKey = new SecretKeySpec(decryptedKey , 0, decryptedKey .length, "AES");
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] bytePlainText = aesCipher.doFinal(EncryptedFile);
            String plainText = new String(bytePlainText); 
            return plainText;   
    }
             public static byte[] RSA_Decryption(PrivateKey AliceprivateKey ,byte [] encryptedKey) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
         Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         cipher.init(Cipher.PRIVATE_KEY, AliceprivateKey);
         byte[] decryptedKey = cipher.doFinal(encryptedKey);
         return decryptedKey;
    }
               public static void callDec(byte[] encryptedKey) throws Exception , NoSuchAlgorithmException 
    {
        Decryption n= new Decryption();
        n.Dec(encryptedKey);
    }
             
             public static void main(String[] args) throws Exception, NoSuchAlgorithmException  {

    Alice c= new Alice();
    String encryptedKey = readFile("encryptedKey.txt");
    c.callDec(encryptedKey.getBytes()); // here is the problem 
    
    }
}
