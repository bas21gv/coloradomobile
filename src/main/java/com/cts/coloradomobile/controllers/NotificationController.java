package com.cts.coloradomobile.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.cts.coloradomobile.pojo.Device;
import com.cts.coloradomobile.pojo.FCMConfig;
import com.cts.coloradomobile.pojo.Notification;
import com.cts.coloradomobile.service.NotificationService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/rest")
public class NotificationController {

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	FCMConfig fcmConfig;

	@Autowired
	NotificationService notificationService;

	@RequestMapping(value = "/sendnotify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> pushNotification(@RequestBody String notifyMsg) {

		log.info("<---- Stared the pushNotification ---->");
		log.info("Request JSON value ::: " + notifyMsg);
		ResponseEntity<?> response = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(notifyMsg, Map.class);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Google Cloud Messaging ::: FCM URL ::: " + fcmConfig.getUrlPath());
		log.info("Google Cloud Messaging ::: FCM API KEY ::: " + fcmConfig.getApiKey());

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "key=" + fcmConfig.getApiKey());

		JSONObject responseJson = new JSONObject();
		List<String> registerIds = notificationService.deviceIds();
		log.info("Registered Id from DB ::: " + registerIds.toString());

		if (!registerIds.isEmpty()) {
			JSONArray idList = new JSONArray(registerIds);
			JSONObject requestJson = new JSONObject();
			requestJson.put("registration_ids", idList);
			requestJson.put("notification", map.get("notification"));/** {"title":"","description":""} **/

			HttpEntity<String> entity = new HttpEntity<String>(requestJson.toString(), headers);
			log.info("Json to send FCM ::: " + entity.getBody());

			try {
				response = restTemplate.exchange(new URI(fcmConfig.getUrlPath()), HttpMethod.POST, entity,
						Object.class);
			} catch (RestClientException | URISyntaxException e) {
				log.error(e.getMessage());
				response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
			log.info("Json received from FCM ::: " + response.getBody());

			LinkedHashMap<String, Object> responsePayload = (LinkedHashMap<String, Object>) response.getBody();
			if ((int) responsePayload.get("success") > 0) {
				Notification notification = new Notification();
				notification.setDeviceId(map.get("deviceid").toString());
				notification.setMessage(map.get("notification").toString());
				boolean result = notificationService.saveNotification(notification);
				if (result) {
					responseJson.put("status", 200);
					responseJson.put("message", "Notification sent");
					response = new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
				}
			}
		} else {
			responseJson.put("status", 500);
			responseJson.put("message", "Notification not sent");
			response = new ResponseEntity<>(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("<---- Ended the pushNotification ---->");
		return response;
	}

	@RequestMapping(value = "/saveid", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveDeviceId(@RequestBody String userDeviceId) {

		log.info("<---- Started Register Ids ---->");
		log.info("Request JSON value ::: " + userDeviceId);

		ResponseEntity<?> response = null;
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = null;
		try {
			map = mapper.readValue(userDeviceId, Map.class);
		} catch (JsonParseException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (JsonMappingException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			log.error(e.getMessage());
			response = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Device device = new Device();
		device.setDeviceId(map.get("deviceid").toString());
		boolean result = notificationService.saveDeviceId(device);
		JSONObject responseJson = new JSONObject();
		if (result) {
			responseJson.put("status", 200);
			responseJson.put("message", "DeviceId registered");
			response = new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} else {
			responseJson.put("status", 500);
			responseJson.put("message", "DeviceId not registered");
			response = new ResponseEntity<>(responseJson.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("<---- Ended Register Ids ---->");
		return response;
	}

	@RequestMapping(value = "/getnotify", method = RequestMethod.GET)
	public ResponseEntity<?> getNotifications() {
		log.info("<---- Started Fetch Notification ---->");
		ResponseEntity<?> response = null;
		List<Object> notifyList = notificationService.notifyMsg();
		JSONObject responseJson = new JSONObject();
		log.info("Notification count from DB ::: " + notifyList.size());
		if (!notifyList.isEmpty()) {
			JSONArray msgList = new JSONArray(notifyList);
			responseJson.put("messages", msgList);
			response = new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		} else {
			responseJson.put("status", 200);
			responseJson.put("message", "No Notification available");
			response = new ResponseEntity<>(responseJson.toString(), HttpStatus.OK);
		}
		log.info("<---- Ended Fetch Notification ---->");
		return response;
	}

}
