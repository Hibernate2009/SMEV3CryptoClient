package com.bssys.smev3.crypto.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class PropertiesService {
	private Properties init() throws FileNotFoundException, UnsupportedEncodingException, IOException {
		Reader reader = null;
		Properties propertyFSSP = new Properties();
		try {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			reader = new InputStreamReader(inputStream, "UTF-8");
			propertyFSSP.load(reader);
		} finally {
			if (reader != null)
				reader.close();
		}
		
		return propertyFSSP;
	}

	public String get(String key) {
		String res = "";
		try {
			Properties propertyFSSP = init();
			res = propertyFSSP.getProperty(key);
			if (res == null || "".equals(res)) {
				System.out.println("property with name: " + key + " not found");
			} else {
				res = res.trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}
	
	public File getFile(String key) {
		String res = "";
		try {
			Properties propertyFSSP = init();
			res = propertyFSSP.getProperty(key);
			if (res == null || "".equals(res)) {
				System.out.println("property with name: " + key + " not found");
			} else {
				res = res.trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new File(res);
    }

}
