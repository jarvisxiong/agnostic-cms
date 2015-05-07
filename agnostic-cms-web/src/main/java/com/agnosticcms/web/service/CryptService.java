package com.agnosticcms.web.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * Service for encryption and decryption functionality
 */
@Service
public class CryptService {

	/**
	 * Computes base64 of a sha1 of a string 
	 * @param string The input string for computations
	 * @return SHA1 digest encoded in base64 format
	 */
	public String getSha1Base64(String string) {
		return Base64.encodeBase64String(DigestUtils.sha1(string));
	}
	
	/**
	 * Same as {@link #getSha1Base64(String)}, but prepends a prefix understandable
	 * by apache HTTP server
	 */
	public String getSha1Base64ForApache(String string) {
		return "{SHA}" + getSha1Base64(string);
	}
	
}
