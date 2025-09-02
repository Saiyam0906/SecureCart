package com.example.SecureCart.Security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {
	
	private KeyUtils() {}

	public static PrivateKey loadPrivateKey(final String pempath) throws Exception{
		final String key=readKey(pempath)
				.replace("-----BEGIN PRIVATE KEY-----", "")
				 .replace("-----END PRIVATE KEY-----", "")
				 .replaceAll("\\s", "");
		
		final byte[] decoded=Base64.getDecoder().decode(key);
		final PKCS8EncodedKeySpec spec=new PKCS8EncodedKeySpec(decoded);
		return KeyFactory.getInstance("RSA").generatePrivate(spec);
	}
	
	public static PublicKey loadPublicKey(final String pempath) throws Exception{
		final String key=readKey(pempath)
				.replace("-----BEGIN PUBLIC KEY-----", "")
				 .replace("-----END PUBLIC KEY-----", "")
				 .replaceAll("\\s", "");
		
		final byte[] decoded=Base64.getDecoder().decode(key);
		final X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
		return KeyFactory.getInstance("RSA").generatePublic(spec);
	}
	
	
	public static String readKey(final String pempath) throws Exception{
		try(final InputStream is=KeyUtils.class.getResourceAsStream("/" + pempath)){
			if(is==null) {
				throw new IllegalArgumentException("Could not find key: " + pempath);
			}
			return new String(is.readAllBytes());
		}
	}
}
