
package is_project;


   
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.Base64;


public class Bob {
   //1- generate public and private key for Bob 
   static KeyPair keyPair = genKeyPair(1024);
   static PublicKey BobpublicKey=keyPair.getPublic();

    static String RandomKey ="mykey";
    static final String IV = "AAACCCDDDYYUURRS"; // 16 byte
 // encrypt and decrypt need the same IV.
 // initialization vector of the same size as the key, 
 
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        PrivateKey BobprivateKey = keyPair.getPrivate();
       
        try {
            test_encrypt_decrypt();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    
        byte[] encryptedBytes = RSAencrypt(RandomKey.getBytes(),Alice.AlicepublicKey);
        PrintWriter writer = new PrintWriter("RSA_encryptedKey.txt", "UTF-8"); // now this is the cipher text
        writer.print(encryptedBytes);
        writer.close();
        System.out.print(encryptedBytes);
  
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
       public static String base64Encode(byte[] src) {
        return new String(Base64.getEncoder().encode(src));
    }

    public static void test_encrypt_decrypt() throws Exception {
        // encrypt "in.txt" -> "out.txt" means convert the plaintext to cipher text
        
        // 2- generate random key then encrypt the file by this key usint AES 
        String s = readFile("PlainText1.txt"); // the plain text
        String res = AESencrypt(RandomKey , IV, s); // "s= plaintext
        PrintWriter writer = new PrintWriter("CipherText.txt", "UTF-8"); // now this is the cipher text
        writer.print(res);
        writer.close();
        System.out.println("Encryption the file:");
        System.out.print("The plain Text is : "+s);
        System.out.println("The cipher Text is : "+res);
   
       
    }
      public static void encrypt(String key, File inputFile, File outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
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
      static void doCrypto(int cipherMode, String key, File inputFile,
                          File outputFile) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
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
    
    
    
    
    
    
  