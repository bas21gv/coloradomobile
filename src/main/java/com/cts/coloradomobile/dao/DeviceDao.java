package com.cts.coloradomobile.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.coloradomobile.pojo.Device;

public interface DeviceDao extends JpaRepository<Device, Integer>{
	
	@Query("SELECT d FROM Device d WHERE d.deviceid =: deviceid")
    public Device findByDeviceId(@Param("deviceid") String deviceid);
}
