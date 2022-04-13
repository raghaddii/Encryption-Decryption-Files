package is_project;
  
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;


public class Alice {
   //1- generate public and private key for Alice  
   static KeyPair keyPair = genKeyPair(4096);
   static PublicKey AlicepublicKey=keyPair.getPublic();
   
       
  public static void main(String[] args) throws Exception {
  
   PrivateKey AliceprivateKey = keyPair.getPrivate();
   
  
   
    String a = readFile2("RSA_encryptedKey.txt"); 
    System.out.println(a);
     
     
     byte[] decryptedBytes = RSAdecrypt(a.getBytes(),AliceprivateKey);
    System.out.println(decryptedBytes);
    String s=new String(decryptedBytes);
   // PrivateKey newKey =loadPrivateKey(s);
    
  //  String b = readFile2("CipherText.txt"); 
 //   String res = decrypt(s, Bob.IV, b);
//System.out.println(res);
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

  
  
public static byte[] RSAdecrypt(byte[] content, PrivateKey privateKey) {
         try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
          
             return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
             System.out.println("here ");
            return null;
        }
    }


      
     public static String readFile2(String filename) throws Exception {
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
       public static String decrypt(String key, String iv, String encrypted) throws Exception {
        byte[] bytesOfKey = key.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] keyBytes = md.digest(bytesOfKey);

        final byte[] ivBytes = iv.getBytes();

        final byte[] encryptedBytes = Base64.getMimeDecoder().decode(encrypted);

        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));

        final byte[] resultBytes = cipher.doFinal(encryptedBytes);
        return new String(resultBytes);
    }
    


}