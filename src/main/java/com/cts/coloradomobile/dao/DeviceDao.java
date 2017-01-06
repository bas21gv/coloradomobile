package com.cts.coloradomobile.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cts.coloradomobile.pojo.Device;

@Transactional
public interface DeviceDao extends CrudRepository<Device,Integer>{
	
	public Device findByDeviceId(String deviceId);
}
