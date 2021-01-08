package com.kg.web.util;

public class KeyUtil {

	
	public static String getURIKey(String key) {
		return key.replace("/", "__");
	}
}
