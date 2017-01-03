package com.cts.coloradomobile.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.coloradomobile.dao.DeviceDao;
import com.cts.coloradomobile.dao.NotificationDao;
import com.cts.coloradomobile.pojo.Device;
import com.cts.coloradomobile.pojo.Notification;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private Notification notification;
	
	@Autowired
	private Device device;
	
	
	public boolean saveNotification(String deviceId,String message){
		device = deviceDao.findByDeviceId(deviceId);
		if(device.getId()!=null){
			notification.setDeviceId(deviceId);
			notification.setMessage(message);
			notificationDao.save(notification);
			return true;
		}
		return false;
	}
	
	public boolean saveDeviceId(String deviceId){
		if(deviceId != null){
			device.setDeviceId(deviceId);
			deviceDao.save(device);
			return true;
		}
		return false;
	}
	
	public List<String> deviceIds(){
		List<String> ids = new ArrayList<>();
		for (Device device : deviceDao.findAll()) {
			ids.add(device.getDeviceId());
		}
		return ids;
	}
}
