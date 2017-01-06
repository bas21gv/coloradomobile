package com.cts.coloradomobile.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="device")
public class Device implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1837973306944376873L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column
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
