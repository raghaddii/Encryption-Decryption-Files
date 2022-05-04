# Encryption-Decryption-Files
##CS334-Project
The project is to make a secure connection between Bob (Sender) and Alice (Receiver) in sending text and a secure key.
The message that was sent contains two files. 1st represent the text message that Bob sent and encrypted by the random key using the AES approach. 
The 2nd represents the receiver public key which is encrypted using the RSA method by the random key.
The decryption works in reverse, where the key is decrypted first using RSA by the private key of the receiver. Then the ciphertext will be decrypted by the decrypted key.
## How It's Used
- Download the zip file
- Open it in any Java platform supporter
- Take the IP of the other device which represents the receiver. (it could be used in a single device)
- Run the code synchronizing, starting with Bob the Alice
- You can now observe the Encryption/Decryption process from the code

## Prepared By:
- Areej Turky Alotaibi              atsalotaibi03@sm.imamu.edu.sa
- Raghad Adel Alshabana         raaalshabana@sm.imamu.edu.sa
- Shoroog Saad Alarifi              sssalarifi@sm.imamu.edu.sa
- Slima Mohammed Bnous	  smbjlal@sm.imamu.edu.sa
