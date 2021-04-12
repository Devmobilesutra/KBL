package com.emsphere.commando4.kirloskarempowerapp.encryptionanddecryption;

import android.util.Base64;
import android.util.Log;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;

import java.io.UnsupportedEncodingException;
import java.security.CryptoPrimitive;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class AESAlgorithm
{

	public String Encrypt(String key)
	{
		/*String EncryptionKey = "!SMS@16%04#qwrty";
		byte[] clearBytes = Encoding.Unicode.GetBytes(text);
		using (Aes encryptor = Aes.Create())
		{
			Rfc2898DeriveBytes pdb = new Rfc2898DeriveBytes(EncryptionKey, new byte[] { 0x49, 0x76, 0x61, 0x6e, 0x20, 0x4d, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 });
			encryptor.Key = pdb.GetBytes(32);
			encryptor.IV = pdb.GetBytes(16);
			using (MemoryStream ms = new MemoryStream())
			{
				using (CryptoStream cs = new CryptoStream(ms, encryptor.CreateEncryptor(), CryptoStreamMode.Write))
				{
					cs.Write(clearBytes, 0, clearBytes.Length);
					cs.Close();
				}
				text = Convert.ToBase64String(ms.ToArray());
			}
		}
		*/

		String cipherText = null;
		Cipher cipher = null;
		//CryptoPrimitive cipher1 = null;
		Key aesKey = new SecretKeySpec(EmpowerApplication.SessionKey.getBytes(),"AES");
		try {
			cipher = Cipher.getInstance("AES");
			byte[] clearTextBytes = key.getBytes("UTF8");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] cipherBytes = cipher.doFinal(clearTextBytes);
			BASE64EncoderStream b = new BASE64EncoderStream(null);
			//Base64.encodeBytes(results);
		    cipherText = new String(b.encode(cipherBytes), "UTF8");
			//cipherText = new String(Base64.encode(cipherBytes,0), "UTF8");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("E1:"+e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("E2:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("E3:"+e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.out.println("E4:"+e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			System.out.println("E5:"+e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("E6:"+e.getMessage());
		}

		return cipherText;
	}

	public String Decrypt(String key)
	{
		String decryptedText = null;
		Cipher cipher = null;

		Key aesKey = new SecretKeySpec(EmpowerApplication.SessionKey.getBytes(),"AES");
		try {
			cipher = Cipher.getInstance("AES");
			BASE64DecoderStream b = new BASE64DecoderStream(null);

			byte[] clearTextBytes = b.decode(key.getBytes("UTF8"));
           // byte[] clearTextBytes = Base64.decode(key.getBytes("UTF8"),0);

			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			byte[] decryptedBytes = cipher.doFinal(clearTextBytes);


			decryptedText  = new String(decryptedBytes, "UTF8");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("E1:"+e.getMessage());
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("E2:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("E3:"+e.getMessage());
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			System.out.println("E4:"+e.getMessage());
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			System.out.println("E5:"+e.getMessage());
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			System.out.println("E6:"+e.getMessage());
		}
		return decryptedText;
	}


	public  String Decrypt1(String text, String key) throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

		BASE64DecoderStream decoder = new BASE64DecoderStream(null);
		byte [] results = cipher.doFinal(decoder.decode(text.getBytes()));
		return new String(results,"UTF-8");
	}

	String Encrypt(String text, String key)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] keyBytes= new byte[16];
		byte[] b= key.getBytes("UTF-8");
		int len= b.length;
		if (len > keyBytes.length) len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

		byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		BASE64EncoderStream encoder = new BASE64EncoderStream(null);
		return encoder.encode(results).toString();
	}

    public static String Decrypt12(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance
                ("AES/CBC/PKCS5Padding"); //this parameters should not be changed
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = new byte[text.length()];
        //BASE64DecoderStream decoder = new BASE64DecoderStream(null);
        try {
            results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT));
        } catch (Exception e) {
            Log.i("Erron in Decryption", e.toString());
        }
        Log.i("Data", new String(results, "UTF-8"));
        return new String(results, "UTF-8"); // it returns the result as a String
    }

    public static String Encrypt12(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        //java.util.Base64 encoder = new android.util.Base64();
        return Base64.encodeToString(results,Base64.DEFAULT); // it returns the result as a String
    }

}