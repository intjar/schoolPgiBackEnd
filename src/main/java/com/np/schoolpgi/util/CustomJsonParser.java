package com.np.schoolpgi.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

@Component
public class CustomJsonParser {
	
	public JSONParser customJsonParserMethod(){
		return new JSONParser();
	}
	
	public JSONObject getParsedDataAsJson(String encryptData) throws ParseException {
		
		return (JSONObject)customJsonParserMethod().parse(encryptData);
	}
	
}
