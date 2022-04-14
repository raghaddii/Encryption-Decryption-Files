package is_project;
  
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Alice {
   //1- generate public and private key for Alice  
   static KeyPair keyPair = genKeyPair(128);
   static PublicKey AlicepublicKey=keyPair.getPublic();
   
   
       
  public static void main(String[] args) throws Exception {
  
   PrivateKey AliceprivateKey = keyPair.getPrivate();
   
String a = readFile2("EncryptedKey.txt"); 
System.out.println(a);



Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");  
cipher.init(Cipher.PRIVATE_KEY, AliceprivateKey);
byte[] decryptedKey = cipher.doFinal(a.getBytes());
 

// System.out.println(array);
   //RSAdecrypt(a.getBytes(),AliceprivateKey);
   // String s = new String(decryptedBytes, StandardCharsets.UTF_8);
   // System.out.println(s);

 //String originalString = new String(Base64.getDecoder().decode(decryptedBytes));
  //System.out.println(originalString);
 
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

    	public static void RSAdecrypt(byte[] data,PrivateKey AliceprivateKey) throws IOException {
		System.out.println("\n----------------DECRYPTION STARTED------------");
		byte[] descryptedData = null;
		
		try {
			PrivateKey privateKey = AliceprivateKey;
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			descryptedData = cipher.doFinal(data);
			System.out.println("Decrypted Data: " + new String(descryptedData));
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		System.out.println("----------------DECRYPTION COMPLETED------------");		
	}
  /*
  public static byte[] decrypt(String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
 Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encStr = cipher.doFinal(data.getBytes());
        return encStr;
}




/*
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


   */

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