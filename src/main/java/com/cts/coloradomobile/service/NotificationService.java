package com.cts.coloradomobile.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coloradomobile.dao.DeviceDao;
import com.cts.coloradomobile.dao.NotificationDao;
import com.cts.coloradomobile.pojo.Device;
import com.cts.coloradomobile.pojo.Notification;

@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	@Autowired
	NotificationDao notificationDao;
	@Autowired
	DeviceDao deviceDao;

	public boolean saveNotification(Notification notification) {
		Device device = deviceDao.findByDeviceId(notification.getDeviceId());
		if (device != null) {
			notificationDao.save(notification);
			log.info("<---- Notification message stored in Database ---->");
			return true;
		}
		return false;
	}

	public boolean saveDeviceId(Device device) {
		if (device != null) {
			deviceDao.save(device);
			log.info("<---- DeviceId registered in Database ---->");
			return true;
		}
		return false;
	}

	public List<String> deviceIds() {
		List<String> ids = new ArrayList<>();
		for (Device device : deviceDao.findAll()) {
			ids.add(device.getDeviceId());
		}
		log.info("<---- fetched all registered Ids ---->");
		return ids;
	}

	public List<Object> notifyMsg() {
		List<Object> msgList = new ArrayList<>();
		for (Notification notification : notificationDao.findAll()) {
			msgList.add(notification.getMessage());
		}
		log.info("<---- fetched all Notification messages ---->");
		return msgList;
	}
}
