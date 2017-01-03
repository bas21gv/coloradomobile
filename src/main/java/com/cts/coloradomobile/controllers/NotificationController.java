package com.cts.coloradomobile.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cts.coloradomobile.service.NotificationService;

@RestController
@RequestMapping("/rest")
@PropertySource("classpath:config.properties")
public class NotificationController {
	
	public final static String AUTH_KEY_FCM = "${fcm.api_key}";
	public final static String API_URL_FCM = "${fcm.url}";
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping(value="/notify", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pushNotification(@RequestBody String message){

		ResponseEntity<Object> response = null;
		String deviceId = null;
		String notification = null;
		if(message != null){
			JSONParser parser = new JSONParser();
			JSONObject requestPayload = null;
			try {
				requestPayload = (JSONObject) parser.parse(message);
				deviceId = requestPayload.get("deviceid").toString();
				notification = requestPayload.get("notification").toString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "key="+AUTH_KEY_FCM);
		
		JSONObject info = new JSONObject();
		info.put("title", "Notification Title");   // Notification title
		info.put("body", "HI, Happy New Year 2017"); // Notification body
		
		List<String> idList = new ArrayList<>();
		idList.add("11vd11vvd2323");
		idList.add("5454541vd11vvd2323");
		JSONObject requestJson = new JSONObject();
		requestJson.put("registration_ids", idList.toString()); /** to - for single sender notificationService.deviceIds()*/
		requestJson.put("notification", info);

		
		HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);
		
		System.out.println(entity.getBody());
		//ResponseEntity<Object> response = restTemplate.postForEntity(new URI(API_URL_FCM), request, Object.class);
		try {
			response = restTemplate.exchange(new URI(API_URL_FCM), HttpMethod.POST, entity, Object.class);
		} catch (RestClientException | URISyntaxException e) {
			e.printStackTrace();
		}
//		if(response.getStatusCodeValue() == 200){
//			boolean result = notificationService.saveNotification(deviceId, notification);
//			if(result){
//				return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
//			}
//		}
		System.out.println(response.getBody());
		return new ResponseEntity<>(response.getBody(),response.getStatusCode());
	}
	
	@RequestMapping(value="/saveid",method=RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDeviceId(@RequestBody String userDeviceId){
		System.out.println(userDeviceId);
		if(userDeviceId != null){
			JSONParser parser = new JSONParser();
			JSONObject requestPayload = null;
			try {
				requestPayload = (JSONObject) parser.parse(userDeviceId);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			boolean result = notificationService.saveDeviceId(requestPayload.get("deviceId").toString());
			if(result){
				return new ResponseEntity<>("Device Id Stored",HttpStatus.OK);
			}else{
				return new ResponseEntity<>("Device Id not Stored",HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<>("Device Id not found",HttpStatus.NOT_FOUND);
	}
	
}
