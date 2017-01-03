package com.cts.coloradomobile.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="device")
public class Device {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="gen_id")
	private Integer id;
	
	@Column(name="device_id", nullable=false)
	private String deviceId;
	
	public Device() {
	}

	public Device(Integer id, String deviceId) {
		this.id = id;
		this.deviceId = deviceId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String toString() {
		return "Device [id=" + id + ", deviceId=" + deviceId + "]";
	}
	
}
