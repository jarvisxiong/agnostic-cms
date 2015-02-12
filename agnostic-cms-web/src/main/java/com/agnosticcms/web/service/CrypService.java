package com.agnosticcms.web.service;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class CrypService {

	public String getSha1Base64(String string) {
		return Base64.encodeBase64String(DigestUtils.sha1(string));
	}
	
	public String getSha1Base64ForApache(String string) {
		return "{SHA}" + getSha1Base64(string);
	}
	
}
